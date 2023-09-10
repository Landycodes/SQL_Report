package SQLReport;

import lombok.AllArgsConstructor;
import  lombok.Data;

@Data
@AllArgsConstructor
public class Employee {
    private String employeeName;
    private String employeeRole;
    private String employeeManager;
    private int employeeSalary;
}
