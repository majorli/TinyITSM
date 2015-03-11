package com.jeans.tinyitsm.action.admin;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.model.view.HRUnit;
import com.jeans.tinyitsm.service.asset.AssetConstants;
import com.jeans.tinyitsm.service.asset.AssetService;
import com.jeans.tinyitsm.service.hr.HRService;
import com.jeans.tinyitsm.util.ExcelUtil;

public class DataImportAction extends TinyAction {

	private HRService hrService;
	private AssetService assetService;

	@Autowired
	public void setHrService(HRService hrService) {
		this.hrService = hrService;
	}

	@Autowired
	public void setAssetService(AssetService assetService) {
		this.assetService = assetService;
	}

	private File data;
	private String dataContentType;
	private String dataFileName;

	public File getData() {
		return data;
	}

	public void setData(File data) {
		this.data = data;
	}

	public String getDataContentType() {
		return dataContentType;
	}

	public void setDataContentType(String dataContentType) {
		this.dataContentType = dataContentType;
	}

	public String getDataFileName() {
		return dataFileName;
	}

	public void setDataFileName(String dataFileName) {
		this.dataFileName = dataFileName;
	}

	private Map<String, Object> results = new HashMap<String, Object>();

	public Map<String, Object> getResults() {
		return results;
	}

	public void setResults(Map<String, Object> results) {
		this.results = results;
	}

	private boolean checkDataFile() {
		if (null == dataContentType || null == dataFileName || null == data) {
			results.put("code", 1);
			results.put("tip", "上传的文件无效");
			return false;
		}
		if (data.length() > 104857600) {
			results.put("code", 2);
			results.put("tip", "上传文件超过长度限制，单个文件大小不能超过100M");
			return false;
		}
		if (!dataContentType.equals("application/vnd.ms-excel") && !dataContentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			results.put("code", 3);
			results.put("tip", "批量导入数据必须采用Excel文档");
			return false;
		}
		return true;
	}

	@Action(value = "hr-import", results = { @Result(name = SUCCESS, type = "json", params = { "root", "results", "contentType", "text/plain", "encoding",
			"UTF-8" }) })
	public String uploadHRData() throws Exception {
		if (!checkDataFile()) {
			return SUCCESS;
		}
		int deptCount = 0, emplCount = 0;
		try (Workbook workBook = WorkbookFactory.create(data)) {
			Sheet deptSheet = workBook.getSheet("部门");
			Sheet emplSheet = workBook.getSheet("员工");
			if (null == deptSheet || null == emplSheet) {
				results.put("code", 4);
				results.put("tip", "导入的数据文件格式不正确，缺少必要的Sheet");
				return SUCCESS;
			}
			HRUnit comp = getCurrentCompany();
			// deptSheet: 从第1行开始为有效数据行，从第0列到第4列为数据列，一共5列，分别为部门全称、部门简称、上级部门、排列序号和是否导入的标志
			int last = deptSheet.getLastRowNum();
			for (int rn = 1; rn <= last; rn++) {
				Row r = deptSheet.getRow(rn);
				// 获取是否导入的标志，如果为"是"则继续导入本行，否则就认为不需要导入本行，跳过
				String flag = StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(4, Row.RETURN_BLANK_AS_NULL)));
				if (!"是".equals(flag))
					continue;
				// 获取部门全称name，不能为空，为空则跳过本行
				String name = ExcelUtil.getCellValueAsString(r.getCell(0, Row.RETURN_BLANK_AS_NULL));
				if (StringUtils.isBlank(name))
					continue;
				else
					name = StringUtils.trim(name);
				// 获取部门简称alias，为空则取部门全称的值
				String alias = ExcelUtil.getCellValueAsString(r.getCell(1, Row.RETURN_BLANK_AS_NULL));
				if (StringUtils.isBlank(alias))
					alias = name;
				else
					alias = StringUtils.trim(alias);
				// 获取上级部门ID(superiorId)，上级部门名称未指定的视为本公司一级部门，指定了上级部门名称但是在本公司找不到这个部门的数据无效，跳过本行
				String superior = ExcelUtil.getCellValueAsString(r.getCell(2, Row.RETURN_BLANK_AS_NULL));
				long superiorId = 0;
				if (StringUtils.isBlank(superior)) {
					superiorId = comp.getId();
				} else {
					HRUnit suprDept = hrService.getDepartmentByName(comp.getId(), superior);
					if (null == suprDept)
						continue;
					else
						superiorId = suprDept.getId();
				}
				// 获取部门的排列序号listOrder，未指定或不能转换为short类型数值的视为127，指定了但不符合规范的规范化为1或者127
				short listOrder = 127;
				try {
					double order = (double) ExcelUtil.getCellValue(r.getCell(3, Row.RETURN_BLANK_AS_NULL));
					if (order < 1)
						listOrder = 1;
					else if (order > 127)
						listOrder = 127;
					else
						listOrder = (short) Math.round(order);
				} catch (ClassCastException e) {
					log(e);
					listOrder = 127;
				}
				hrService.appendDept(name, alias, superiorId, listOrder);
				deptCount++;
			}
			// emplSheet: 从第1行开始为有效数据行，从第0列到第4列为数据列，一共4列，分别为员工姓名、所在部门、排列序号和是否导入的标志
			last = emplSheet.getLastRowNum();
			for (int rn = 1; rn <= last; rn++) {
				Row r = emplSheet.getRow(rn);
				// 获取是否导入的标志，如果为"是"则继续导入本行，否则就认为不需要导入本行，跳过
				String flag = StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(3, Row.RETURN_BLANK_AS_NULL)));
				if (!"是".equals(flag))
					continue;
				// 获取员工姓名name，不能为空，为空则跳过本行
				String name = ExcelUtil.getCellValueAsString(r.getCell(0, Row.RETURN_BLANK_AS_NULL));
				if (StringUtils.isBlank(name))
					continue;
				else
					name = StringUtils.trim(name);
				// 获取所属部门ID(deptId)，未指定的或找不到指定的部门的均视为数据无效，跳过本行
				String deptName = ExcelUtil.getCellValueAsString(r.getCell(1, Row.RETURN_BLANK_AS_NULL));
				long deptId = 0;
				if (StringUtils.isBlank(deptName)) {
					continue;
				} else {
					HRUnit dept = hrService.getDepartmentByName(comp.getId(), deptName);
					if (null == dept)
						continue;
					else
						deptId = dept.getId();
				}
				// 获取员工的排列序号listOrder，未指定或不能转换为short类型数值的视为127，指定了但不符合规范的规范化为1或者127
				short listOrder = 127;
				try {
					double order = (double) ExcelUtil.getCellValue(r.getCell(2, Row.RETURN_BLANK_AS_NULL));
					if (order < 1)
						listOrder = 1;
					else if (order > 127)
						listOrder = 127;
					else
						listOrder = (short) Math.round(order);
				} catch (ClassCastException e) {
					log(e);
					listOrder = 127;
				}
				hrService.appendEmpl(name, deptId, listOrder);
				emplCount++;
			}
			results.put("code", 0);
			results.put("tip", "批量数据导入成功，总共导入" + deptCount + "个部门，" + emplCount + "名员工");
			log("批量HR数据导入成功，总共导入" + deptCount + "个部门，" + emplCount + "名员工");
			return SUCCESS;
		} catch (Exception e) {
			log(e);
			results.put("code", 4);
			results.put("tip", "导入时发现无法识别的文档格式或数据项，已导入" + deptCount + "个部门，" + emplCount + "名员工");
			log("批量HR数据导入发生错误，总共导入" + deptCount + "个部门，" + emplCount + "名员工");
			return SUCCESS;
		}
	}

	@Action(value = "ci-import", results = { @Result(name = SUCCESS, type = "json", params = { "root", "results", "contentType", "text/plain", "encoding",
			"UTF-8" }) })
	public String uploadCIData() throws Exception {
		if (!checkDataFile()) {
			return SUCCESS;
		}
		int hardCount = 0, softCount = 0;
		try (Workbook workBook = WorkbookFactory.create(data)) {
			Sheet hardSheet = workBook.getSheet("硬件");
			Sheet softSheet = workBook.getSheet("软件");
			if (null == hardSheet || null == softSheet) {
				results.put("code", 4);
				results.put("tip", "导入的数据文件格式不正确，缺少必要的Sheet");
				return SUCCESS;
			}
			HRUnit comp = getCurrentCompany();
			ExcelUtil.setNumberFormat("#");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年mm月");
			// hardSheet: 从第1行开始为有效数据行，从第0列到第15列为数据列，一共16列，第0列为是否导入的标志
			int last = hardSheet.getLastRowNum();
			for (int rn = 1; rn <= last; rn++) {
				Row r = hardSheet.getRow(rn);
				// 获取是否导入的标志，如果为"是"则继续导入本行，否则就认为不需要导入本行，跳过
				String flag = StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(0, Row.RETURN_BLANK_AS_NULL)));
				// 获取资产名称name，不能为空，为空则跳过本行
				String name = StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(3, Row.RETURN_BLANK_AS_NULL)));
				if (!"是".equals(flag) || StringUtils.isBlank(name)) {
					continue;
				}
				Map<String, Object> hardware = new HashMap<String, Object>();
				hardware.put("companyId", comp.getId());
				hardware.put("type", AssetConstants.HARDWARE_ASSET);
				hardware.put("name", name);
				hardware.put("code", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(1, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("catalog", parseAssetCatalog(StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(2, Row.RETURN_BLANK_AS_NULL))), AssetConstants.HARDWARE_ASSET));
				hardware.put("vendor", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(4, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("modelOrVersion", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(5, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("assetUsage", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(6, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("purchaseTime", ExcelUtil.getCellValueAsDate(r.getCell(7, Row.RETURN_BLANK_AS_NULL), sdf));
				try {
					double q = (double) ExcelUtil.getCellValue(r.getCell(8, Row.RETURN_BLANK_AS_NULL));
					if (q < 1) {
						hardware.put("quantity", 1);
					} else {
						hardware.put("quantity", (int) Math.round(q));
					}
				} catch (Exception e) {
					hardware.put("quantity", 1);
				}
				try {
					hardware.put("cost", BigDecimal.valueOf((double) ExcelUtil.getCellValue(r.getCell(9, Row.RETURN_BLANK_AS_NULL))));
				} catch (Exception e) {
					hardware.put("cost", new BigDecimal(0));
				}
				hardware.put("state", AssetConstants.IDLE);
				hardware.put("sn", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(10, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("configuration", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(11, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("warranty", AssetConstants.IMPLIED_WARRANTY);
				hardware.put("location", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(12, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("ip", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(13, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("importance", AssetConstants.GENERAL_DEGREE);
				hardware.put("ownerId", 0L);
				hardware.put("comment", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(14, Row.RETURN_BLANK_AS_NULL))));
				hardware.put("financialCode", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(15, Row.RETURN_BLANK_AS_NULL))));
				
				assetService.newAsset(hardware, AssetConstants.HARDWARE_ASSET);
				hardCount++;
			}
			// softSheet: 从第1行开始为有效数据行，从第0列到第12列为数据列，一共13列，第0列为是否导入的标志
			last = softSheet.getLastRowNum();
			for (int rn = 1; rn <= last; rn++) {
				Row r = softSheet.getRow(rn);
				// 获取是否导入的标志，如果为"是"则继续导入本行，否则就认为不需要导入本行，跳过
				String flag = StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(0, Row.RETURN_BLANK_AS_NULL)));
				// 获取软件名称name，不能为空，为空则跳过本行
				String name = StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(2, Row.RETURN_BLANK_AS_NULL)));
				if (!"是".equals(flag) || StringUtils.isBlank(name)) {
					continue;
				}
				if (StringUtils.isBlank(name))
					continue;
				Map<String, Object> software = new HashMap<String, Object>();
				software.put("companyId", comp.getId());
				software.put("type", AssetConstants.SOFTWARE_ASSET);
				software.put("name", name);
				software.put("catalog", parseAssetCatalog(StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(1, Row.RETURN_BLANK_AS_NULL))), AssetConstants.SOFTWARE_ASSET));
				software.put("vendor", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(3, Row.RETURN_BLANK_AS_NULL))));
				software.put("modelOrVersion", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(4, Row.RETURN_BLANK_AS_NULL))));
				try {
					double q = (double) ExcelUtil.getCellValue(r.getCell(5, Row.RETURN_BLANK_AS_NULL));
					if (q < 1) {
						software.put("quantity", 1);
					} else {
						software.put("quantity", (int) Math.round(q));
					}
				} catch (Exception e) {
					software.put("quantity", 1);
				}
				try {
					software.put("cost", BigDecimal.valueOf((double) ExcelUtil.getCellValue(r.getCell(6, Row.RETURN_BLANK_AS_NULL))));
				} catch (Exception e) {
					software.put("cost", new BigDecimal(0));
				}
				software.put("purchaseTime", ExcelUtil.getCellValueAsDate(r.getCell(7, Row.RETURN_BLANK_AS_NULL), sdf));
				software.put("assetUsage", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(8, Row.RETURN_BLANK_AS_NULL))));
				software.put("state", AssetConstants.IN_USE);
				software.put("softwareType", parseSoftwareType(StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(9, Row.RETURN_BLANK_AS_NULL)))));
				software.put("license", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(10, Row.RETURN_BLANK_AS_NULL))));
				software.put("expiredTime", ExcelUtil.getCellValueAsDate(r.getCell(11, Row.RETURN_BLANK_AS_NULL), sdf));
				software.put("comment", StringUtils.trim(ExcelUtil.getCellValueAsString(r.getCell(12, Row.RETURN_BLANK_AS_NULL))));
				
				assetService.newAsset(software, AssetConstants.SOFTWARE_ASSET);
				softCount++;
			}
			results.put("code", 0);
			results.put("tip", "批量数据导入成功，总共导入" + hardCount + "项硬件类资产，" + softCount + "项软件类资产");
			log("批量CI数据导入成功，总共导入" + hardCount + "项硬件类资产，" + softCount + "项软件类资产");
			return SUCCESS;
		} catch (Exception e) {
			log(e);
			results.put("code", 4);
			results.put("tip", "导入时发现无法识别的文档格式或数据项，已导入" + hardCount + "项硬件类资产，" + softCount + "项软件类资产");
			log("批量CI数据导入发生错误，总共导入" + hardCount + "项硬件类资产，" + softCount + "项软件类资产");
			return SUCCESS;
		}
	}

	private byte parseAssetCatalog(String catalogName, byte assetType) {
		if (assetType == AssetConstants.GENERIC_ASSET) {
			return AssetConstants.GENERIC_IT_ASSET;
		} else if (assetType == AssetConstants.HARDWARE_ASSET) {
			byte r = AssetConstants.OTHER_EQUIPMENT;
			switch (catalogName) {
			case "网络设备":
				r = AssetConstants.NETWORK_EQUIPMENT;
				break;
			case "安全设备":
				r = AssetConstants.SECURITY_EQUIPMENT;
				break;
			case "服务器":
				r = AssetConstants.SERVER_EQUIPMENT;
				break;
			case "存储设备":
				r = AssetConstants.STORAGE_EQUIPMENT;
				break;
			case "机房基础设施":
				r = AssetConstants.INFRASTRUCTURE_EQUIPMENT;
				break;
			case "桌面终端":
				r = AssetConstants.TERMINATOR_EQUIPMENT;
				break;
			case "移动终端":
				r = AssetConstants.MOBILE_EQUIPMENT;
				break;
			case "打印设备":
				r = AssetConstants.PRINTER_EQUIPMENT;
				break;
			default:
				r = AssetConstants.OTHER_EQUIPMENT;
			}
			return r;
		} else if (assetType == AssetConstants.SOFTWARE_ASSET) {
			byte r = AssetConstants.OTHER_SOFTWARE;
			switch (catalogName) {
			case "操作系统":
				r = AssetConstants.OPERATING_SYSTEM_SOFTWARE;
				break;
			case "数据库":
				r = AssetConstants.DATABASE_SYSTEM_SOFTWARE;
				break;
			case "中间件":
				r = AssetConstants.MIDDLEWARE_SOFTWARE;
				break;
			case "存储备份":
				r = AssetConstants.STORAGE_SYSTEM_SOFTWARE;
				break;
			case "安全管理":
				r = AssetConstants.SECURITY_SOFTWARE;
				break;
			case "办公软件":
				r = AssetConstants.OFFICE_SOFTWARE;
				break;
			case "应用软件":
				r = AssetConstants.APPLICATION_SOFTWARE;
				break;
			default:
				r = AssetConstants.OTHER_SOFTWARE;
			}
			return r;
		} else {
			return AssetConstants.GENERIC_IT_ASSET;
		}
	}

	private byte parseSoftwareType(String softwareTypeName) {
		byte r = AssetConstants.COMMERCIAL_SOFTWARE;
		switch (softwareTypeName) {
		case "商品软件":
			r = AssetConstants.COMMERCIAL_SOFTWARE;
			break;
		case "自由/开源软件":
			r = AssetConstants.OPEN_SOURCE_SOFTWARE;
			break;
		case "免费软件":
			r = AssetConstants.FREE_SOFTWARE;
			break;
		case "试用软件":
			r = AssetConstants.TRIAL_SOFTWARE;
			break;
		case "定制开发软件":
			r = AssetConstants.CUSTOM_DEVELOPED_SOFTWARE;
			break;
		case "自主研发软件":
			r = AssetConstants.SELF_DEVELOPED_SOFTWARE;
			break;
		default:
			r = AssetConstants.OTHER_TYPE_SOFTWARE;
		}
		return r;
	}
}
