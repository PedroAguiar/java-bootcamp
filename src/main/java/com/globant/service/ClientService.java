package com.globant.service;

import com.globant.dto.ClientDTO;
import com.globant.model.Client;

public interface ClientService {

    Client createClient(ClientDTO clientDTO) throws Exception;

    Client getClient(String clientId) throws Exception;

    Client updateClient(ClientDTO clientDTO) throws Exception;

    void deleteClient(String clientId) throws Exception;
}
