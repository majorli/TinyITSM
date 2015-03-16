package com.jeans.tinyitsm.action.asset;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeans.tinyitsm.action.TinyAction;
import com.jeans.tinyitsm.model.view.AssetItem;
import com.jeans.tinyitsm.model.view.HardwareItem;
import com.jeans.tinyitsm.model.view.SoftwareItem;
import com.jeans.tinyitsm.service.asset.AssetConstants;
import com.jeans.tinyitsm.service.asset.AssetService;

public class AssetExportAction extends TinyAction {

	private AssetService service;

	@Autowired
	public void setService(AssetService service) {
		this.service = service;
	}

	private String type;
	private String filename;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	private boolean isIE() {
		String ua = ServletActionContext.getRequest().getHeader("user-agent").toUpperCase();
		if ((ua != null) && (ua.indexOf("MSIE") != -1 || ua.indexOf("TRIDENT") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	private void export(HttpServletResponse resp) throws IOException {
		StringBuilder fn = new StringBuilder(getCurrentCompany().getName());
		Workbook wb = new XSSFWorkbook();
		if ("_hard".equals(type)) {
			fn.append(" - 硬件类资产清单(");
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.NETWORK_EQUIPMENT)), AssetConstants.NETWORK_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.SECURITY_EQUIPMENT)), AssetConstants.SECURITY_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.SERVER_EQUIPMENT)), AssetConstants.SERVER_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.STORAGE_EQUIPMENT)), AssetConstants.STORAGE_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.INFRASTRUCTURE_EQUIPMENT)), AssetConstants.INFRASTRUCTURE_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.TERMINATOR_EQUIPMENT)), AssetConstants.TERMINATOR_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.MOBILE_EQUIPMENT)), AssetConstants.MOBILE_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.PRINTER_EQUIPMENT)), AssetConstants.PRINTER_EQUIPMENT);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.OTHER_EQUIPMENT)), AssetConstants.OTHER_EQUIPMENT);
		} else if ("_soft".equals(type)) {
			fn.append(" - 软件类资产清单(");
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.OPERATING_SYSTEM_SOFTWARE)),
					AssetConstants.OPERATING_SYSTEM_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.DATABASE_SYSTEM_SOFTWARE)), AssetConstants.DATABASE_SYSTEM_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.MIDDLEWARE_SOFTWARE)), AssetConstants.MIDDLEWARE_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.STORAGE_SYSTEM_SOFTWARE)), AssetConstants.STORAGE_SYSTEM_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.SECURITY_SOFTWARE)), AssetConstants.SECURITY_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.OFFICE_SOFTWARE)), AssetConstants.OFFICE_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.APPLICATION_SOFTWARE)), AssetConstants.APPLICATION_SOFTWARE);
			generateSheet(wb.createSheet(AssetConstants.getAssetCatalogName(AssetConstants.OTHER_SOFTWARE)), AssetConstants.OTHER_SOFTWARE);
		} else {
			fn.append(" - IT资产清单(");
			generateSheet(wb.createSheet(AssetConstants.getAssetTypeName(AssetConstants.HARDWARE_ASSET)), AssetConstants.HARDWARE_ASSET);
			generateSheet(wb.createSheet(AssetConstants.getAssetTypeName(AssetConstants.SOFTWARE_ASSET)), AssetConstants.SOFTWARE_ASSET);
		}
		fn.append((new SimpleDateFormat("yyyyMMdd")).format(new Date())).append(").xlsx");
		if (isIE()) {
			filename = URLEncoder.encode(fn.toString(), "UTF-8").replaceAll("\\+", "%20");
		} else {
			filename = new String(fn.toString().getBytes("UTF-8"), "iso8859-1");
		}
		resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		resp.setHeader("Content-disposition", "attachment; filename=" + filename);
		BufferedOutputStream out = new BufferedOutputStream(resp.getOutputStream(), 4096);
		wb.write(out);
		wb.close();
		out.close();
	}

	private void generateSheet(Sheet sheet, byte type) {
		// 写sheet内容
		if (type == AssetConstants.HARDWARE_ASSET) {
			// 全部硬件
			generateSheetHeader(sheet, true);
		} else if (type == AssetConstants.SOFTWARE_ASSET) {
			// 全部软件
			generateSheetHeader(sheet, false);
		} else if (type >= AssetConstants.NETWORK_EQUIPMENT && type <= AssetConstants.OTHER_EQUIPMENT) {
			// 某个硬件类别
			generateSheetHeader(sheet, true);
		} else if (type >= AssetConstants.OPERATING_SYSTEM_SOFTWARE && type <= AssetConstants.OTHER_SOFTWARE) {
			// 某个软件类别
			generateSheetHeader(sheet, false);
		}
		List<AssetItem> assets = service.loadAssets(getCurrentCompanyId(), type);
		int rowNumber = 1;
		for (AssetItem asset : assets) {
			appendRow(sheet, asset, rowNumber++);
		}
	}

	private static String[] HARDWARE_HEADERS = new String[] { "资产编号", "财务资产编号", "所属公司", "资产类别", "名称", "制造商/品牌", "型号", "用途及基本功能", "序列号", "主要配置", "采购时间", "数量",
			"原值", "使用情况", "保修状态", "物理位置", "网络地址", "重要程度", "责任人", "备注" };
	private static int[] HARDWARE_HEADERS_WIDTH = new int[] { 12, 12, 12, 12, 20, 12, 24, 60, 24, 60, 11, 5, 13, 9, 9, 30, 16, 9, 9, 40 };
	private static String[] SOFTWARE_HEADERS = new String[] { "所属公司", "资产类别", "名称", "软件厂商", "软件版本", "用途及基本功能", "采购时间", "数量", "原值", "使用情况", "软件类型", "许可证",
			"许可期限", "备注" };
	private static int[] SOFTWARE_HEADERS_WIDTH = new int[] { 12, 12, 20, 12, 24, 60, 11, 5, 13, 9, 14, 20, 11, 40 };

	private void appendRow(Sheet sheet, AssetItem asset, int rowNumber) {
		// 添加一行
		DataFormat df = sheet.getWorkbook().createDataFormat();
		// 行字体：宋体、10号
		Font font = sheet.getWorkbook().createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		// 格式1，用于文本：上下居中、左右居中、行字体、文本、不自动换行
		CellStyle cellStyleString = sheet.getWorkbook().createCellStyle();
		cellStyleString.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleString.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleString.setFont(font);
		cellStyleString.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		cellStyleString.setWrapText(false);
		// 格式2，用于日期：上下居中、左右居中、行字体、日期(yyyy年MM月)、不自动换行
		CellStyle cellStyleDate = sheet.getWorkbook().createCellStyle();
		cellStyleDate.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyleDate.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleDate.setFont(font);
		cellStyleDate.setDataFormat(df.getFormat("yyyy年MM月"));
		cellStyleDate.setWrapText(false);
		// 格式3，用于数量：上下居中、左右居右、行字体、数值(#)、不自动换行
		CellStyle cellStyleQuantity = sheet.getWorkbook().createCellStyle();
		cellStyleQuantity.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleQuantity.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleQuantity.setFont(font);
		cellStyleQuantity.setDataFormat(df.getFormat("#"));
		cellStyleQuantity.setWrapText(false);
		// 格式4，用于原值：上下居中、左右居右、行字体、数值(#,##0.00_ )、不自动换行
		CellStyle cellStyleCost = sheet.getWorkbook().createCellStyle();
		cellStyleCost.setAlignment(CellStyle.ALIGN_RIGHT);
		cellStyleCost.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyleCost.setFont(font);
		cellStyleCost.setDataFormat(df.getFormat("#,##0.00_ "));
		cellStyleCost.setWrapText(false);
		// 行高20
		Row row = sheet.createRow(rowNumber);
		row.setHeightInPoints(20);
		Cell cell = null;
		if (asset instanceof HardwareItem) {
			cell = row.createCell(0, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getCode());

			cell = row.createCell(1, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getFinancialCode());

			cell = row.createCell(2, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getCompany());

			cell = row.createCell(3, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getCatalog());

			cell = row.createCell(4, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getName());

			cell = row.createCell(5, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getVendor());

			cell = row.createCell(6, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getModelOrVersion());

			cell = row.createCell(7, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getAssetUsage());

			cell = row.createCell(8, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getSn());

			cell = row.createCell(9, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getConfiguration());

			cell = row.createCell(10, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleDate);
			Date pt = ((HardwareItem) asset).getPurchaseTime();
			if (null != pt) {
				cell.setCellValue(pt);
			}

			cell = row.createCell(11, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleQuantity);
			cell.setCellValue(((HardwareItem) asset).getQuantity());

			cell = row.createCell(12, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleCost);
			cell.setCellValue(((HardwareItem) asset).getCost().doubleValue());

			cell = row.createCell(13, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getState());

			cell = row.createCell(14, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getWarranty());

			cell = row.createCell(15, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getLocation());

			cell = row.createCell(16, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getIp());

			cell = row.createCell(17, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getImportance());

			cell = row.createCell(18, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getOwner());

			cell = row.createCell(19, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((HardwareItem) asset).getComment());
		} else if (asset instanceof SoftwareItem) {
			cell = row.createCell(0, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getCompany());

			cell = row.createCell(1, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getCatalog());

			cell = row.createCell(2, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getName());

			cell = row.createCell(3, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getVendor());

			cell = row.createCell(4, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getModelOrVersion());

			cell = row.createCell(5, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getAssetUsage());

			cell = row.createCell(6, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleDate);
			Date pt = ((SoftwareItem) asset).getPurchaseTime();
			if (null != pt) {
				cell.setCellValue(pt);
			}

			cell = row.createCell(7, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleQuantity);
			cell.setCellValue(((SoftwareItem) asset).getQuantity());

			cell = row.createCell(8, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleCost);
			cell.setCellValue(((SoftwareItem) asset).getCost().doubleValue());

			cell = row.createCell(9, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getState());

			cell = row.createCell(10, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getSoftwareType());

			cell = row.createCell(11, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getLicense());

			cell = row.createCell(12, Cell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(cellStyleDate);
			Date et = ((SoftwareItem) asset).getExpiredTime();
			if (null != et) {
				cell.setCellValue(et);
			}

			cell = row.createCell(13, Cell.CELL_TYPE_STRING);
			cell.setCellStyle(cellStyleString);
			cell.setCellValue(((SoftwareItem) asset).getComment());
		}
	}

	private void generateSheetHeader(Sheet sheet, boolean hardware) {
		// 建立标题行
		// 标题字体：宋体、10号、加粗
		Font font = sheet.getWorkbook().createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 10);
		// 标题单元格格式：上下居中、左右居中、标题字体、文本、不自动换行
		CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));
		cellStyle.setWrapText(false);
		// 标题行：行高20
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = null;
		if (hardware) {
			for (int i = 0; i < 20; i++) {
				cell = row.createCell(i, Cell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(HARDWARE_HEADERS[i]);
				sheet.setColumnWidth(i, HARDWARE_HEADERS_WIDTH[i] * 256);
			}
		} else {
			for (int i = 0; i < 14; i++) {
				cell = row.createCell(i, Cell.CELL_TYPE_STRING);
				cell.setCellStyle(cellStyle);
				cell.setCellValue(SOFTWARE_HEADERS[i]);
				sheet.setColumnWidth(i, SOFTWARE_HEADERS_WIDTH[i] * 256);
			}
		}
	}

	@Action(value = "export-assets")
	public String exportAssets() throws Exception {
		if (StringUtils.isBlank(type)) {
			type = "_all";
		}
		HttpServletResponse resp = ServletActionContext.getResponse();
		export(resp);
		return null;
	}
}
