package com.example.deleever;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.net.ssl.SSLContext;

public class Order_card {
    @SerializedName("customer")
    private Customer customer;
    @SerializedName("product")
    private Product product;
    @SerializedName("deliveryAddress")
    private String deliveryAddress;
    @SerializedName("orderId")
    private int orderId;

    @SerializedName("quantity")
    private int quantity;
    @SerializedName("orderStatus")
    private String status;
    @SerializedName("paymentMethod")
    private String paymentMethod;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @SerializedName("createdAt")
    private String dateAndTime;
    public Order_card(String deliveryAddress, String status, int orderId, Customer customer, Product product, int quantity,String paymentMethod) {
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.orderId = orderId;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.paymentMethod = paymentMethod;

    }
    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    static class Customer {
        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;
        @SerializedName("contactNumber")
        private String contactNumber;

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Customer(String firstName) {
            this.firstName = firstName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }

    static class Product{
        @SerializedName("description")
        private String description;
        @SerializedName("productCode")
        private String productCode;
        @SerializedName("name")

        private String name;

        @SerializedName("price")

        private float price;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String SeparatedateString(String dateAndTime){
        String dateTimeString = getDateAndTime();
        //change to local data time object
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);

        String date = dateTime.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        return date;
    }

    public String SeparateTimeString(String dateAndTime){
        String dateTimeString = getDateAndTime();
        //change to local data time object
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME);

        String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time;
    }

}
