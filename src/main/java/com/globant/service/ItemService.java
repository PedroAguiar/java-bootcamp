package com.globant.service;

import com.globant.dto.ItemDTO;
import com.globant.model.Item;

public interface ItemService {

    Item createItem(ItemDTO itemDTO) throws Exception;

    Item getItem(String itemId) throws Exception;

    Item updateItem(ItemDTO itemDTO) throws Exception;

    void deleteItem(String itemId) throws Exception;
}
