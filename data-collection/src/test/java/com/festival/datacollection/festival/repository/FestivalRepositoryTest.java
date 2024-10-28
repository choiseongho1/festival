package com.festival.datacollection.festival.repository;

import com.festival.datacollection.festival.domain.Festival;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FestivalRepositoryTest {

    @Autowired
    private FestivalRepository festivalRepository;

    @Test
    public void testSaveAndFindFestival() {
        // Given
        Festival festival = new Festival();
        festival.setContentId("12345");
        festival.setTitle("Test Festival");
        festival.setAddr1("서울특별시 강남구");
        festival.setEventStartDate("2024-10-10");
        festival.setEventEndDate("2024-10-12");
        festival.setFirstImage("http://example.com/image1.jpg");
        festival.setMapx(127.123456);
        festival.setMapy(37.123456);
        festival.setTel("02-123-4567");

        // When
        Festival savedFestival = festivalRepository.save(festival);

        // Then
        assertThat(savedFestival.getContentId()).isNotNull();
        assertThat(savedFestival.getTitle()).isEqualTo("Test Festival");
    }
}