package com.lnjoying.justice.commonweb.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Objects;

public class ObjectDiffLogger
{
    public static <T> LinkedHashMap<String, PropertyValueChangeLog> logObjectDiff(T oldObj, T newObj)
    {
        if (oldObj == null || newObj == null)
        {
            return new LinkedHashMap<>(0);
        }
        LinkedHashMap<String, PropertyValueChangeLog> changeLogMap = new LinkedHashMap<>();
        Field[] oldFields = oldObj.getClass().getDeclaredFields();
        Field[] newFields = newObj.getClass().getDeclaredFields();
        for (Field oldField : oldFields)
        {
            Field newField = findCorrespondingField(oldField, newFields);
            if (newField != null)
            {
                try
                {
                    oldField.setAccessible(true);
                    newField.setAccessible(true);
                    Object oldValue = oldField.get(oldObj);
                    Object newValue = newField.get(newObj);

                    if (!Objects.equals(oldValue, newValue))
                    {
                        String fieldName = String.format("%s.%s", oldField.getDeclaringClass().getName(),
                                oldField.getName()).replace(oldObj.getClass().getName() + ".", "");
                        if (!changeLogMap.containsKey(fieldName))
                        {
                            PropertyValueChangeLog propertyValueChangeLog = new PropertyValueChangeLog();
                            propertyValueChangeLog.setOldValue(oldValue);
                            propertyValueChangeLog.setNewValue(newValue);
                            changeLogMap.put(fieldName, propertyValueChangeLog);
                        }
                    }
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return changeLogMap;
    }

    private static Field findCorrespondingField(Field oldField, Field[] newFields)
    {
        for (Field newField : newFields)
        {
            if (newField.getName().equals(oldField.getName()))
            {
                return newField;
            }
        }
        return null;
    }

    public static class PropertyValueChangeLog
    {
        private Object oldValue;
        private String oldValueEnDescription;
        private String oldValueCnDescription;
        private Object newValue;
        private String newValueEnDescription;
        private String newValueCnDescription;

        public Object getOldValue()
        {
            return oldValue;
        }

        public void setOldValue(Object oldValue)
        {
            this.oldValue = oldValue;
        }

        public String getOldValueEnDescription()
        {
            return oldValueEnDescription;
        }

        public void setOldValueEnDescription(String oldValueEnDescription)
        {
            this.oldValueEnDescription = oldValueEnDescription;
        }

        public String getOldValueCnDescription()
        {
            return oldValueCnDescription;
        }

        public void setOldValueCnDescription(String oldValueCnDescription)
        {
            this.oldValueCnDescription = oldValueCnDescription;
        }

        public Object getNewValue()
        {
            return newValue;
        }

        public void setNewValue(Object newValue)
        {
            this.newValue = newValue;
        }

        public String getNewValueEnDescription()
        {
            return newValueEnDescription;
        }

        public void setNewValueEnDescription(String newValueEnDescription)
        {
            this.newValueEnDescription = newValueEnDescription;
        }

        public String getNewValueCnDescription()
        {
            return newValueCnDescription;
        }

        public void setNewValueCnDescription(String newValueCnDescription)
        {
            this.newValueCnDescription = newValueCnDescription;
        }
    }
}