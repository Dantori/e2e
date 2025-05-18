package com.trofimov.e2e;

import com.trofimov.e2e.configuration.FeatureStoreConfiguration;
import com.trofimov.e2e.dto.FeatureDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {FeatureStoreConfiguration.class})
public class FeatureStoreRegressTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private static final String BASE_URL = "http://localhost:8080/api/v1/features";

    @Test
    void testCreateAndGetFeature() {
        FeatureDto newFeature = new FeatureDto();
        newFeature.setName("Regression Test Feature");
        newFeature.setVersion("1.0");
        newFeature.setFeatureValue("Test value");

        ResponseEntity<FeatureDto> postResponse = testRestTemplate.postForEntity(BASE_URL, newFeature, FeatureDto.class);
        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        FeatureDto created = postResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(newFeature.getName());

        // Получаем фичу по ID
        ResponseEntity<FeatureDto> getResponse = testRestTemplate.getForEntity(BASE_URL + "/" + created.getId(), FeatureDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        FeatureDto fetched = getResponse.getBody();
        assertThat(fetched).isNotNull();
        assertThat(fetched.getName()).isEqualTo(newFeature.getName());
    }

    @Test
    void testUpdateFeature() {
        // Создаем фичу для обновления
        FeatureDto feature = new FeatureDto();
        feature.setName("To Update");
        feature.setVersion("1.0");
        feature.setFeatureValue("Initial value");

        FeatureDto created = testRestTemplate.postForEntity(BASE_URL, feature, FeatureDto.class).getBody();
        assertThat(created).isNotNull();

        // Обновляем
        created.setName("Updated Name");
        created.setFeatureValue("Updated value");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FeatureDto> entity = new HttpEntity<>(created, headers);

        ResponseEntity<Void> putResponse = testRestTemplate.exchange(BASE_URL + "/" + created.getId(), HttpMethod.PUT, entity, Void.class);
        assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Проверяем обновление
        FeatureDto updated = testRestTemplate.getForObject(BASE_URL + "/" + created.getId(), FeatureDto.class);
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getFeatureValue()).isEqualTo("Updated value");
    }

    @Test
    void testDeleteFeature() {
        // Создаем фичу
        FeatureDto feature = new FeatureDto();
        feature.setName("To Delete");
        feature.setVersion("1.0");
        feature.setFeatureValue("Delete me");

        FeatureDto created = testRestTemplate.postForEntity(BASE_URL, feature, FeatureDto.class).getBody();
        assertThat(created).isNotNull();

        // Удаляем
        testRestTemplate.delete(BASE_URL + "/" + created.getId());

        // Проверяем, что фича удалена
        ResponseEntity<FeatureDto> getResponse = testRestTemplate.getForEntity(BASE_URL + "/" + created.getId(), FeatureDto.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
