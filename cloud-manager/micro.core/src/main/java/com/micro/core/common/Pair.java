package com.micro.core.common;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Regulus
 * @Date: 12/18/21 4:35 PM
 * @Description:
 */
@Data
@NoArgsConstructor
public class Pair<KeyType, ValueType>
{
    public Pair(KeyType key, ValueType value)
    {
        this.key = key;
        this.value = value;
    }
    KeyType key;
    ValueType value;
    
    public KeyType getLeft() {return key;}
    public ValueType getRight() {return value;}
}
