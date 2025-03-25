package com.example.demo.service.impl;

import com.example.demo.model.ExampleEntity;
import com.example.demo.repository.EntityRepository;
import com.example.demo.service.EntityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EntityServiceImpl implements EntityService {
    private final EntityRepository repository;
    private final EntityManager entityManager;

    @Transactional(readOnly = true)
    @Override
    public void getAll() {
        ObjectMapper mapper = new ObjectMapper();

        File file = new File("Example.json");

        try(Stream<ExampleEntity> entityStream = repository.findAllBy();
            SequenceWriter writer = mapper.writer().writeValues(file)
        ) {
            writer.init(true);
            entityStream.forEach(entity -> {
                try {
                    writer.write(entity);
                    entityManager.detach(entity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }
}
