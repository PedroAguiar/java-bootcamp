package com.globant.service.impl;

import com.globant.dto.ClientDTO;
import com.globant.model.Client;
import com.globant.model.Payment;
import com.globant.repository.ClientRepository;
import com.globant.repository.PaymentRepository;
import com.globant.service.ClientService;
import com.globant.util.EncryptingUtil;
import com.globant.util.ModelUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, PaymentRepository paymentRepository) {
        this.clientRepository = clientRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Client saveClient(ClientDTO clientDTO) throws Exception {
        Validate.notNull(clientDTO);
        return clientRepository.save(ModelUtils.toClient.apply(clientDTO));
    }

    @Override
    public Client getClient(String clientId) throws Exception {
        return clientRepository.getOne(EncryptingUtil.decryptId(clientId));
    }

    @Override
    public Client updateClient(ClientDTO clientDTO) throws Exception {
        Validate.notNull(clientDTO);
        Validate.notEmpty(clientDTO.getPaymentIds());
        Client client = getClient(clientDTO.getClientId());
        List<Payment> payments = clientDTO.getPaymentIds().stream()
                .map(paymentId -> paymentRepository.getOne(EncryptingUtil.decryptId(paymentId)))
                .collect(Collectors.toList());
        client.setName(clientDTO.getName());
        client.setLastName(clientDTO.getLastName());
        client.setPayments(payments);
        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(String clientId) throws Exception {
        clientRepository.delete(EncryptingUtil.decryptId(clientId));
    }

}
