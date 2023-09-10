package SQLReport;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class SqlReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqlReportApplication.class, args);

		String filePath = "src\\main\\resources\\templates\\SQL_Report.jrxml";
		JRBeanCollectionDataSource reportDatasource = null;
		List<Employee> employeeList = new ArrayList<>();
		String name;
		String role;
		String manager;
		int salary;

		String jdbcUrl = "jdbc:mysql://localhost:3306/employee_db";
		String username = "root";
		String password = "";

		try {
			Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
			System.out.println("DB connection established!");
			Statement statement = connection.createStatement();
//			String employeeTable = "select * from employee";
			String employeeTable = "select concat(last_name, ', ', first_name) as name from employee;";
			ResultSet results = statement.executeQuery(employeeTable);

			while (results.next()) {
//				int ID = results.getInt("id");
				name = results.getString("name");

				employeeList.add(new Employee(name, "f", "test", 1) );
				System.out.println(name);
				System.out.println();

				//logic for retrieving data and creating new employee for it here
				// create an employee object for each row
				//add each employee object to the employee list
				reportDatasource = new JRBeanCollectionDataSource(employeeList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("reportTitle", "Employee CMS report");
		parameters.put("ReportDataset", reportDatasource);
		System.out.println(reportDatasource.getData());

		try {
			JasperReport report = JasperCompileManager.compileReport(filePath);
			JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			JasperExportManager.exportReportToPdfFile(print, "src\\main\\resources\\static\\SQL_Report.pdf");
			System.out.println("Report created!");
		} catch (JRException e) {
			throw new RuntimeException(e);
		}
	}

}
