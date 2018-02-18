package com.globant.service.impl;

import com.globant.dto.ItemDTO;
import com.globant.model.Item;
import com.globant.repository.ItemRepository;
import com.globant.service.ItemService;
import com.globant.util.EncryptingUtil;
import com.globant.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item createItem(ItemDTO itemDTO) throws Exception {
        final Item item = ModelUtils.toItem(itemDTO);
        return itemRepository.save(item);
    }

    @Override
    public Item getItem(String itemId) throws Exception {
        return itemRepository.getOne(EncryptingUtil.decryptId(itemId));
    }

    @Override
    public Item updateItem(ItemDTO itemDTO) throws Exception {
        final Item item = ModelUtils.toItem(itemDTO);
        return itemRepository.save(item);
    }

    @Override
    public void deleteItem(String itemId) throws Exception {
        itemRepository.delete(EncryptingUtil.decryptId(itemId));
    }
}
