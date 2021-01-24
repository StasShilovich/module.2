package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.dao.exception.DaoException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TagServiceImplTest {

    @Mock
    TagDao tagDao;
    TagDTOMapper tagConverter;
    TagService tagService;
    Tag correctTag;
    TagDTO correctTagDTO;

    @BeforeEach
    public void setUp() {
        tagConverter = new TagDTOMapper();
        tagService = new TagServiceImpl(tagDao, tagConverter);
        correctTag = new Tag(1L, "name");
        correctTagDTO = new TagDTO(1L, "name");
    }

    @Test
    void testFindTagPositive() throws ServiceException, DaoException {
        lenient().when(tagDao.read(anyLong())).thenReturn(Optional.of(correctTag));
        TagDTO actual = tagService.find(1L);
        assertEquals(correctTagDTO, actual);
    }

    @Test
    void testFindTagNegative() throws ServiceException, DaoException {
        lenient().when(tagDao.read(anyLong())).thenReturn(Optional.of(correctTag));
        TagDTO actual = tagService.find(1L);
        TagDTO expected = new TagDTO(1L, "names");
        assertNotEquals(expected, actual);
    }

    @Test
    void testFindTagServiceException() throws DaoException {
        lenient().when(tagDao.read(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.find(1L));
    }

    @Test
    void testAddPositive() throws ServiceException, DaoException {
        lenient().when(tagDao.create(any(Tag.class))).thenReturn(Optional.of(correctTag));
        TagDTO actual = tagService.add(correctTagDTO);
        assertEquals(correctTagDTO, actual);
    }

    @Test
    void testAddNegative() throws DaoException, ServiceException {
        lenient().when(tagDao.create(any(Tag.class))).thenReturn(Optional.of(correctTag));
        TagDTO actual = tagService.add(correctTagDTO);
        assertNotEquals(new TagDTO(), actual);
    }

    @Test
    void testAddServiceException() throws DaoException {
        lenient().when(tagDao.create(any(Tag.class))).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.add(correctTagDTO));
    }

    @Test
    void testDeleteServiceException() throws DaoException {
        lenient().when(tagDao.delete(anyLong())).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.delete(1L));
    }

    @Test
    void testDeleteNotExistEntityException() throws DaoException {
        lenient().when(tagDao.delete(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotExistEntityException.class, () -> tagService.delete(1L));
    }

    @Test
    void testFindAllPositive() throws DaoException, ServiceException {
        List<Tag> tags = new ArrayList<>();
        tags.add(correctTag);
        lenient().when(tagDao.findAll()).thenReturn(Optional.of(tags));
        List<TagDTO> actual = tagService.findAll();
        List<TagDTO> expected = new ArrayList<>();
        expected.add(correctTagDTO);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllNegative() throws DaoException, ServiceException {
        List<Tag> tags = new ArrayList<>();
        tags.add(correctTag);
        lenient().when(tagDao.findAll()).thenReturn(Optional.of(tags));
        List<TagDTO> actual = tagService.findAll();
        assertNotEquals(new ArrayList<>().size(), actual.size());
    }

    @Test
    void testFindAllException() throws DaoException {
        lenient().when(tagDao.findAll()).thenThrow(DaoException.class);
        assertThrows(ServiceException.class, () -> tagService.findAll());
    }
}
