package com.bookkeeping.bookmanagement;

import com.bookkeeping.bookmanagement.book.service.AIChatService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class BookManagementSystemApplicationTests {

    @MockBean
    private AIChatService aiChatService;

    @Test
    void contextLoads() {
    }

}
