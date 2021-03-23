package com.geekbrains.geekspring.soap;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.entities.ProductImage;
import com.geekbrains.geekspring.repositories.ProductRepository;
import com.geekbrains.geekspring.soap.gen.*;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.httpclient.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.FileRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

@Endpoint
public class ProductsEndpoint {

    private static final String NAMESPACE_URI = "http://www.geekbrains.com/geekspring/soap/gen";

    private ProductRepository productRepository;

    @Autowired
    public ProductsEndpoint(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // веб сервис, который возвращает объект типа GetAllProductsResponse, внутри которого коллекция из ProductSoap (все товары из БД)
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllProductsRequest")
    @ResponsePayload
    public GetAllProductsResponse getAllProducts(@RequestPayload GetAllProductsRequest request) throws UnirestException {
        GetAllProductsResponse response = new GetAllProductsResponse();

        List<Product> products = (List<Product>) productRepository.findAll();

        for (Product product : products) {

            CategorySoap categorySoap = new CategorySoap();
            categorySoap.setId(product.getCategory().getId());
            categorySoap.setDescription(product.getCategory().getDescription());
            categorySoap.setTitle(product.getCategory().getTitle());

            ProductSoap productSoap = new ProductSoap();

            for (ProductImage productImage : product.getImages()) {

                ProductImagesSoap image = new ProductImagesSoap();
                image.setId(productImage.getId());
                image.setPath(productImage.getPath());

                productSoap.getProductImagesSoap().add(image);
            }

            productSoap.setId(product.getId());
            productSoap.setCategory(categorySoap);
            productSoap.setShortDescription(product.getShortDescription());
            productSoap.setFullDescription(product.getFullDescription());
            productSoap.setPrice(product.getPrice());
            productSoap.setVendor(product.getVendorCode());
            productSoap.setTitle(product.getTitle());


            response.getProductsSoap().add(productSoap);

        }

        return response;
    }
}