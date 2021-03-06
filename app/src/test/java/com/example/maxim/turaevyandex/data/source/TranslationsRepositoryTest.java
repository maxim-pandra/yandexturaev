package com.example.maxim.turaevyandex.data.source;

import com.example.maxim.turaevyandex.data.Translation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */

public class TranslationsRepositoryTest {

    private TranslationsRepository translationsRepository;

    @Mock
    private TranslationsDataSource translationRemoteDataSource;

    @Mock
    private TranslationsDataSource translationLocalDataSource;


    @Before
    public void setupTranslationRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        translationsRepository = TranslationsRepository.getInstance(
                translationRemoteDataSource, translationLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        TranslationsRepository.destroyInstance();
    }

    @Test
    public void saveTranslation_savesTranslationToDb() {

        Translation translation = new Translation("hello", "здравствуйте", "en-ru");

        translationsRepository.saveTranslation(translation);

        verify(translationLocalDataSource).saveTranslation(translation);
    }
}
