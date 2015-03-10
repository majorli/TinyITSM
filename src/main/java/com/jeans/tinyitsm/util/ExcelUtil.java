package com.jeans.tinyitsm.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

public class ExcelUtil {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static DecimalFormat numberFormat = null;
	private static boolean chineseBoolean = true;
	private static boolean evaluateFormula = true;

	/**
	 * 设置日期格式，null表示采用"年-月-日 时:分:秒"的默认格式
	 * 
	 * @param dateFormatString
	 */
	public static void setDateFormat(String dateFormatString) {
		try {
			dateFormat = new SimpleDateFormat(dateFormatString);
		} catch (Exception e) {
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	}

	/**
	 * 获取当前使用的日期格式化器
	 * 
	 * @return
	 */
	public static SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	/**
	 * 设置数字格式，null表示取消数字格式化
	 * 
	 * @param numberFormatString
	 */
	public static void setNumberFormat(String numberFormatString) {
		try {
			numberFormat = new DecimalFormat(numberFormatString);
		} catch (Exception e) {
			numberFormat = null;
		}
	}

	/**
	 * 获取当前使用的数字格式化器，默认不格式化数字，所以要注意：可能会返回null
	 * 
	 * @return
	 */
	public static DecimalFormat getNumberFormat() {
		return numberFormat;
	}

	/**
	 * 是否采用中文表示布尔值
	 * 
	 * @return
	 */
	public static boolean isChineseBoolean() {
		return chineseBoolean;
	}

	public static void setChineseBoolean(boolean bool) {
		chineseBoolean = bool;
	}

	/**
	 * 是否计算出公式的值，还是直接返回公式字符串
	 * 
	 * @return
	 */
	public static boolean isEvaluateFormula() {
		return evaluateFormula;
	}

	public static void setEvaluateFormula(boolean bool) {
		evaluateFormula = bool;
	}

	/**
	 * 计算一个单元格里的公式值并返回，可能的返回类型有String, Double, Date, Boolean，如果计算出错则返回字符串"ERROR#{errCode}"
	 * 
	 * @param cell
	 *            单元格
	 * @return
	 */
	public static Object evaluateCell(Cell cell) {
		if (null == cell) {
			return "";
		}
		FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
		CellValue cellValue = evaluator.evaluate(cell);
		Object ret = null;
		switch (cellValue.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			ret = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell))
				ret = cell.getDateCellValue();
			else
				ret = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			ret = "ERROR#" + cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_FORMULA:
		default:
			ret = "";
		}
		return ret;
	}

	/**
	 * 计算一个单元格里的公式值，并转换为字符串返回，如果计算出错则返回字符串"ERROR#{errCode}"
	 * 
	 * @param cell
	 *            单元格
	 * @return
	 */
	public static String evaluateCellToString(Cell cell) {
		if (null == cell) {
			return "";
		}
		FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
		CellValue cellValue = evaluator.evaluate(cell);
		String ret = null;
		switch (cellValue.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			ret = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell))
				ret = dateFormat.format(cell.getDateCellValue());
			else
				ret = numberFormat == null ? Double.toString(cell.getNumericCellValue()) : numberFormat.format(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = chineseBoolean ? (cell.getBooleanCellValue() ? "是" : "否") : Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			ret = "ERROR#" + cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
		case Cell.CELL_TYPE_FORMULA:
		default:
			ret = "";
		}
		return ret;
	}

	/**
	 * 返回一个单元格的值，值的类型可能有String, Double, java.util.Date, Boolean, Byte(单元格包含错误信息时的错误码)五种，单元格为null的返回空字符串<br>
	 * 公式单元格返回字符串形式的公式还是返回公式的计算值由ExcelUtil.evaluateFormula的值确定<br>
	 * 含有错误的单元格返回字符串"ERROR#{errCode}"
	 * 
	 * @param cell
	 *            单元格
	 * @return
	 */
	public static Object getCellValue(Cell cell) {
		Object ret = null;
		if (null == cell) {
			return "";
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			ret = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell))
				ret = cell.getDateCellValue();
			else
				ret = cell.getNumericCellValue();
			break;
		case Cell.CELL_TYPE_FORMULA:
			if (evaluateFormula)
				ret = evaluateCell(cell);
			else
				ret = ":= " + cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = cell.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_ERROR:
			ret = "ERROR#" + cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			ret = "";
			break;
		default:
		}
		return ret;
	}

	/**
	 * 以字符串方式返回一个单元格的值，单元格为null时返回空字符串
	 * 公式单元格返回字符串形式的公式还是返回公式的计算值由ExcelUtil.evaluateFormula的值确定
	 * 含有错误的单元格返回字符串"ERROR#{errCode}"
	 * 
	 * @param cell
	 *            单元格
	 * @return
	 */
	public static String getCellValueAsString(Cell cell) {
		String ret = null;
		if (null == cell) {
			return "";
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			ret = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell))
				ret = dateFormat.format(cell.getDateCellValue());
			else
				ret = numberFormat == null ? Double.toString(cell.getNumericCellValue()) : numberFormat.format(cell.getNumericCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:
			if (evaluateFormula)
				ret = evaluateCellToString(cell);
			else
				ret = ":= " + cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			ret = chineseBoolean ? (cell.getBooleanCellValue() ? "是" : "否") : Boolean.toString(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_ERROR:
			ret = "ERROR#" + cell.getErrorCellValue();
			break;
		case Cell.CELL_TYPE_BLANK:
			ret = "";
			break;
		default:
		}
		return ret;
	}

	/**
	 * 强行获取一个单元格的内容为Date类型数据
	 * 
	 * @param cell
	 *            单元格
	 * @param df
	 *            用于解析字符串日期值的日期格式
	 * @return
	 */
	public static Date getCellValueAsDate(Cell cell, SimpleDateFormat df) {
		Date ret = null;
		if (null != cell) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				try {
					ret = df.parse(cell.getStringCellValue());
				} catch (ParseException e) {
					ret = null;
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				try {
					ret = cell.getDateCellValue();
				} catch (Exception e) {
					ret = null;
				}
				break;
			default:
				ret = null;
			}
		}
		return ret;
	}
}
