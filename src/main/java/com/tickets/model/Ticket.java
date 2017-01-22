package com.tickets.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "TICKETS")
public class Ticket implements Serializable{
    private int id;
    private String number;
    private String title;
    private String owner;
    private String cluster;
    private Date openDate;
    private Date closeDate;
    private Date requestDate;
    private Date dueDate;
    private String description;
    private String reportedBy;
    private String priority;
    private String tstatus;
    private String accOwner;

    private static List<String> clusterList,statuses,priorities;

    public Ticket() {
        initModelList();
    }
    public Ticket(int id){
        this.id = id;
    }

    public Ticket(String number, String title, String owner, String cluster, Date openDate, Date closeDate, String description,
                  String reportedBy, String priority, String tstatus, String accOwner, Date requestDate, Date dueDate) {
        this();
        this.number = number;
        this.title = title;
        this.owner = owner;
        this.cluster = cluster;
        this.openDate = openDate;
        this.closeDate = closeDate;
        this.description = description;
        this.reportedBy = reportedBy;
        this.priority = priority;
        this.tstatus = tstatus;
        this.accOwner = accOwner;
        this.requestDate = requestDate;
        this.dueDate = dueDate;
    }

    public void initModelList() {
        clusterList = new ArrayList<String>();
        clusterList.add("BCP");
        clusterList.add("Data Quality");
        clusterList.add("End User Support");
        clusterList.add("Enchancements");
        clusterList.add("Finance");
        clusterList.add("Integration");
        clusterList.add("Masterdata");
        clusterList.add("Order Execution");
        clusterList.add("Planning");
        clusterList.add("Reporting");
        clusterList.add("Tickets");
        clusterList.add("Training");
        clusterList.add("Projects");

        statuses = new ArrayList<String>();
        statuses.add("In queue");
        statuses.add("Open");
        statuses.add("In Progress");
        statuses.add("Ongoing");
        statuses.add("Closed");

        priorities = new ArrayList<String>();
        priorities.add("High");
        priorities.add("Medium");
        priorities.add("Low");
        priorities.add("Critical");

    }

    public static List<String> getClustersList(){return clusterList;}
    public static List<String> getStatusesList(){return statuses;}
    public static List<String> getPrioritiesList(){return priorities;}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "TICKET_NO")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Column(name = "TICKET_TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "TICKET_OWNER")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Column(name = "CLUSTER")
    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setOpenDate(Date date){this.openDate = date;}

    @Column(name = "OPEN_DATE")
    public Date getOpenDate(){return openDate;}

    public void setCloseDate(Date date){this.closeDate = date;}

    @Column(name = "CLOSE_DATE")
    public Date getCloseDate(){return closeDate;}

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String val) {this.description = val;}

    @Column(name = "REPORTED_BY")
    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String val) {
        this.reportedBy = val;
    }

    @Column(name = "PRIORITY")
    public String getPriority() {
        return priority;
    }

    public void setPriority(String val) {
        this.priority = val;
    }

    @Column(name = "STATUS")
    public String getTstatus() {
        return tstatus;
    }

    public void setTstatus(String val) {
        this.tstatus = val;
    }

    @Column(name = "ACC_OWNER")
    public String getAccOwner() {
        return accOwner;
    }

    public void setAccOwner(String val) {
        this.accOwner = val;
    }

    @Column(name = "REQUEST_DATE")
    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    @Column(name = "DUE_DATE")
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
