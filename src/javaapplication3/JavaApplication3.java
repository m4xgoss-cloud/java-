package javaapplication3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


interface Taxable {
    double TAX_RATE = 0.15;

    double calculateTax();
}

interface BonusCalculable {

    double calculateBonus(double performanceScore);
}


abstract class Employee implements Comparable<Employee>, Taxable, BonusCalculable {

    private final String id;
    private final String name;
    protected double baseSalary;
    private final LocalDate joinDate;

    public Employee(String id, String name, double baseSalary, LocalDate joinDate) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.joinDate = joinDate;
    }

    public abstract double calculateNetSalary();

    @Override
    public int compareTo(Employee other) {
        return Double.compare(other.calculateNetSalary(), this.calculateNetSalary());
    }

    @Override
    public double calculateTax() {
        return calculateNetSalary() * TAX_RATE;
    }

    @Override
    public abstract double calculateBonus(double performanceScore);

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBaseSalary() { return baseSalary; }
    public LocalDate getJoinDate() { return joinDate; }

    @Override
    public String toString() {
        return String.format("%-12s | ID: %-5s | Name: %-10s | Join: %s | Net: %10.2f",
                getClass().getSimpleName(), id, name, joinDate, calculateNetSalary());
    }
}


class FullTimeEmployee extends Employee {

    private double allowance;

    public FullTimeEmployee(String id, String name, double baseSalary,
                            LocalDate joinDate, double allowance) {
        super(id, name, baseSalary, joinDate);
        this.allowance = allowance;
    }

    @Override
    public double calculateNetSalary() {
        return baseSalary + allowance;
    }

    @Override
    public double calculateBonus(double performanceScore) {
        return baseSalary * 0.10 * performanceScore;
    }
}


class ContractorEmployee extends Employee {

    private int hourlyRate;
    private int hoursWorked;

    public ContractorEmployee(String id, String name, LocalDate joinDate,
                              int hourlyRate, int hoursWorked) {
        super(id, name, 0, joinDate); 
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateNetSalary() {
        return (double) hourlyRate * hoursWorked;
    }

    @Override
    public double calculateBonus(double performanceScore) {
        return performanceScore > 4.0 ? 500.0 : 0.0;
    }
}


public class JavaApplication3 {

    public static void main(String[] args) {

        List<Employee> employees = new ArrayList<>();
        employees.add(new FullTimeEmployee("F001", "Ahmed", 3000, LocalDate.of(2019, 3, 15), 500));
        employees.add(new FullTimeEmployee("F002", "Sara", 4500, LocalDate.of(2017, 7, 1), 800));
        employees.add(new FullTimeEmployee("F003", "Omar", 2500, LocalDate.of(2022, 1, 10), 300));
        employees.add(new ContractorEmployee("C001", "Laila", LocalDate.of(2021, 5, 20), 40, 120));
        employees.add(new ContractorEmployee("C002", "Khaled", LocalDate.of(2020, 9, 5), 55, 150));
        employees.add(new ContractorEmployee("C003", "Mona", LocalDate.of(2023, 2, 28), 30, 100));

        Map<String, Double> scores = new HashMap<>();
        scores.put("F001", 4.2);
        scores.put("F002", 4.8);
        scores.put("F003", 3.5);
        scores.put("C001", 4.5);
        scores.put("C002", 3.9);
        scores.put("C003", 4.1);

        Collections.sort(employees);
        System.out.println("=== Sorted by Net Salary (Descending) - Comparable ===");
        printList(employees);

        Comparator<Employee> byJoinDate = new Comparator<Employee>() {
            @Override
            public int compare(Employee e1, Employee e2) {
                return e1.getJoinDate().compareTo(e2.getJoinDate());
            }
        };
        employees.sort(byJoinDate);
        System.out.println("\n=== Sorted by Join Date (Oldest First) - Comparator ===");
        printList(employees);

        double totalTax = 0;
        double totalBonus = 0;
        double totalNet = 0;

        System.out.println("\n=== Details per Employee ===");
        System.out.printf("%-10s %-12s %-12s %-12s%n", "Name", "Net Salary", "Tax", "Bonus");
        for (Employee e : employees) {
            double tax = e.calculateTax();
            double bonus = e.calculateBonus(scores.get(e.getId()));
            totalTax += tax;
            totalBonus += bonus;
            totalNet += e.calculateNetSalary();
            System.out.printf("%-10s %-12.2f %-12.2f %-12.2f%n",
                    e.getName(), e.calculateNetSalary(), tax, bonus);
        }

        System.out.println("\n=== Totals ===");
        System.out.printf("Total Net Salaries : %.2f%n", totalNet);
        System.out.printf("Total Tax          : %.2f%n", totalTax);
        System.out.printf("Total Bonuses      : %.2f%n", totalBonus);
        System.out.printf("Total Cost (Tax + Bonus): %.2f%n", totalTax + totalBonus);
    }

    private static void printList(List<Employee> list) {
        for (Employee e : list) {
            System.out.println(e);
        }
    }
}