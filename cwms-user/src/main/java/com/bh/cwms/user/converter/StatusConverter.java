package com.bh.cwms.user.converter;

import com.bh.cwms.user.model.type.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Boolean> {
    /**
     * Java to DB
     *
     * @param status : Java enum for user status
     * @return Boolean: For user status in database
     */
    @Override
    public Boolean convertToDatabaseColumn(Status status) {
        return (status != null)?status.isActive():null;
    }

    /**
     * DB to Java
     *
     * @param aBoolean : DB boolean for user status
     * @return Status Java enum representation of the boolean status
     */
    @Override
    public Status convertToEntityAttribute(Boolean aBoolean) {
        return Status.of(aBoolean);
    }
}
