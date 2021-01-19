package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagConverter;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.ServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TagServiceImplTest {

    @Mock
    TagDao tagDao;
    TagConverter tagConverter;
    TagService tagService;
    Tag correctTag;
    TagDTO correctTagDTO;

    @BeforeEach
    public void setUp() {
        tagConverter = new TagConverter();
        tagService = new TagServiceImpl(tagDao, tagConverter);
        correctTag = new Tag(1L, "name");
        correctTagDTO = new TagDTO(1L, "name");
    }

    @Test
    void testFindTagPositive() throws ServiceException, DaoException {
        lenient().when(tagDao.read(anyLong())).thenReturn(correctTag);
        TagDTO actual = tagService.find(1L);
        assertEquals(actual, correctTagDTO);
    }

    @Test
    void testFindTagNegative() throws ServiceException, DaoException {
        lenient().when(tagDao.read(anyLong())).thenReturn(correctTag);
        TagDTO actual = tagService.find(1L);
        TagDTO expected = new TagDTO(1L, "names");
        assertNotEquals(actual, expected);
    }

    @Test
    void testFindTagException() throws DaoException {
        lenient().when(tagDao.read(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.find(1L));
    }

    @Test
    void testAddPositive() throws ServiceException, DaoException {
        lenient().when(tagDao.create(any(Tag.class))).thenReturn(correctTag);
        TagDTO actual = tagService.add(correctTagDTO);
        assertEquals(actual, correctTagDTO);
    }

    @Test
    void testAddNegative() throws DaoException, ServiceException {
        lenient().when(tagDao.create(any(Tag.class))).thenReturn(correctTag);
        TagDTO actual = tagService.add(correctTagDTO);
        assertNotEquals(actual, new TagDTO());
    }

    @Test
    void testAddException() throws DaoException {
        lenient().when(tagDao.create(any(Tag.class))).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.add(correctTagDTO));
    }

    @Test
    void testDeletePositive() throws DaoException, ServiceException {
        lenient().when(tagDao.delete(anyLong())).thenReturn(1L);
        long actual = tagService.delete(1L);
        assertEquals(actual, 1L);
    }

    @Test
    void testDeleteNegative() throws DaoException, ServiceException {
        lenient().when(tagDao.delete(anyLong())).thenReturn(1L);
        long actual = tagService.delete(1L);
        assertNotEquals(actual, 2L);
    }

    @Test
    void testDeleteException() throws DaoException {
        lenient().when(tagDao.delete(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.delete(1L));
    }
}
