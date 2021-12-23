package com.example.kms;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="kms_products")
public class Product implements Serializable
{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private int productID;
	private String name;
	private String imgPath;
	private double price;
	private String Description;


	public Product()
	{
	}

	public Product(int id,String name, String imgPath, double price, String description) {
		this.productID = id;
		this.name = name;
		this.imgPath = imgPath;
		this.price = price;
		Description = description;
	}


	public void setDescription(String description) {
		Description = description;
	}

	public String getDescription() {
		return Description;
	}

	public String getName() {
	        return this.name;
	    }

	public void setName(String name) {
		this.name = name;
	}

	public String getImgPath() {
		return this.imgPath;
	}

	public void setImgPath(String imgSrc) {
		this.imgPath = imgSrc;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}
}
