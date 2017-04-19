package com.example.maxim.turaevyandex.data.source;

import android.content.ContentResolver;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of the in-memory repository with cache.
 */

public class TranslationRepositoryTest {

    private TranslationRepository translationRepository;

    @Mock
    private TranslationDataSource translationRemoteDataSource;

    @Mock
    private TranslationDataSource translationLocalDataSource;


    @Before
    public void setupTranslationRepository() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        translationRepository = TranslationRepository.getInstance(
                translationRemoteDataSource, translationLocalDataSource);
    }

    @After
    public void destroyRepositoryInstance() {
        TranslationRepository.destroyInstance();
    }

    @Test
    public void saveTranslation_savesTranslationToDb() {

        Translation translation = new Translation("hello", "здравствуйте", "en-ru");

        translationRepository.saveTranslation(Translation translation);

        verify(translationLocalDataSource).saveTranslation(translation);
    }
}
