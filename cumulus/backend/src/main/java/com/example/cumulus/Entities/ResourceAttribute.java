package com.example.cumulus.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "resource_attributes")
public class ResourceAttribute {
    @Id
    private String id;
    @Field("resource_id")
    private String resourceId;
    @Field("attribute_ids")
    private List<String> attributeIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public List<String> getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(List<String> attributeId) {
        this.attributeIds = attributeId;
    }
}
