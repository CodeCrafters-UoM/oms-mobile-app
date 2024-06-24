package com.example.deleever;

import com.google.gson.annotations.SerializedName;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @SerializedName("totalOrdersForCustomer")
    int totalOrdersForCustomer;
    @SerializedName("totalOrdersForCustomerForSeller")
    int totalOrdersForCustomerForSeller;
    @SerializedName("totalReturnOrdersForCustomer")
    int totalReturnOrdersForCustomer;
    @SerializedName("totalReturnOrdersForCustomerForSeller")
    int totalReturnOrdersForCustomerForSeller;
    @SerializedName("createdAt")
    private String dateAndTime;

    public Order_card(Customer customer, Product product, String deliveryAddress, int orderId, int quantity, String status,
                      String paymentMethod, int totalOrdersForCustomer, int totalOrdersForCustomerForSeller,
                      int totalReturnOrdersForCustomer, int totalReturnOrdersForCustomerForSeller, String dateAndTime) {
        this.customer = customer;
        this.product = product;
        this.deliveryAddress = deliveryAddress;
        this.orderId = orderId;
        this.quantity = quantity;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.totalOrdersForCustomer = totalOrdersForCustomer;
        this.totalOrdersForCustomerForSeller = totalOrdersForCustomerForSeller;
        this.totalReturnOrdersForCustomer = totalReturnOrdersForCustomer;
        this.totalReturnOrdersForCustomerForSeller = totalReturnOrdersForCustomerForSeller;
        this.dateAndTime = dateAndTime;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalOrdersForCustomer() {
        return totalOrdersForCustomer;
    }

    public void setTotalOrdersForCustomer(int totalOrdersForCustomer) {
        this.totalOrdersForCustomer = totalOrdersForCustomer;
    }

    public int getTotalOrdersForCustomerForSeller() {
        return totalOrdersForCustomerForSeller;
    }

    public void setTotalOrdersForCustomerForSeller(int totalOrdersForCustomerForSeller) {
        this.totalOrdersForCustomerForSeller = totalOrdersForCustomerForSeller;
    }

    public int getTotalReturnOrdersForCustomer() {
        return totalReturnOrdersForCustomer;
    }

    public void setTotalReturnOrdersForCustomer(int totalReturnOrdersForCustomer) {
        this.totalReturnOrdersForCustomer = totalReturnOrdersForCustomer;
    }

    public int getTotalReturnOrdersForCustomerForSeller() {
        return totalReturnOrdersForCustomerForSeller;
    }

    public void setTotalReturnOrdersForCustomerForSeller(int totalReturnOrdersForCustomerForSeller) {
        this.totalReturnOrdersForCustomerForSeller = totalReturnOrdersForCustomerForSeller;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Method to extract the date part from the dateAndTime string
    public String SeparatedateString(String dateAndTime) {
        // Parse the input dateAndTime string to a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ISO_DATE_TIME);

        // Extract and format the date part
        String date = dateTime.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        return date;
    }

    // Method to extract the time part from the dateAndTime string
    public String SeparateTimeString(String dateAndTime) {
        // Parse the input dateAndTime string to a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(dateAndTime, DateTimeFormatter.ISO_DATE_TIME);

        // Extract and format the time part
        String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return time;
    }

    static class Customer {
        @SerializedName("firstName")
        private String firstName;
        @SerializedName("lastName")
        private String lastName;
        @SerializedName("contactNumber")
        private String contactNumber;

        public Customer(String firstName) {
            this.firstName = firstName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }
    }

    static class Product {
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
}
