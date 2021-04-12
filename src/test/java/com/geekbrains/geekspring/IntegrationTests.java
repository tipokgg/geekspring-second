package com.geekbrains.geekspring;

import com.geekbrains.geekspring.entities.Product;
import com.geekbrains.geekspring.repositories.ProductRepository;
import com.geekbrains.geekspring.services.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    // проверяем, что на странице /cart есть слово "Корзина"
    @Test
    public void correctCart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.core.StringContains.containsString("Корзина")));
    }

    // проверяем, что страница регитсрации возвращает нужную вьюшку
    @Test
    public void testRegistrationPage() throws Exception {
        mockMvc.perform(get("/register/showRegistrationForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("registration-form"));
    }

    // провеяем, что сервис товаров при поиске товара по названию возвращает true если товар найден
    // запрос в базу через репозиторий замокирован
    @Test
    public void isProductWithTitleExistsTest() throws Exception {

        String productName = "product";

        Mockito.doReturn(new Product())
                .when(productRepository)
                .findOneByTitle(productName);

        Assert.assertTrue(productService.isProductWithTitleExists(productName));
    }
}
