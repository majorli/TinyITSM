package com.jeans.tinyitsm.event;

public enum HREventType implements BaseEventType {
	NewDepartmentCreated,
	DepartmentAbandoned,
	NewEmployeeCreated,
	EmployeeMoved,
	EmployeeLeft;

	@Override
	public String getTitle() {
		String ret = null;
		switch (this) {
		case NewDepartmentCreated:
			ret = "新增部门";
			break;
		case DepartmentAbandoned:
			ret = "部门停用";
			break;
		case NewEmployeeCreated:
			ret = "新增员工";
			break;
		case EmployeeMoved:
			ret = "员工调动";
			break;
		case EmployeeLeft:
			ret = "员工离职";
			break;
		default:
			ret = "";
		}
		return ret;
	}
}
