package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.entity.Brand;
import bg.softuni.carsHeaven.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BrandServiceImplTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private BrandServiceImpl brandService;

    public BrandServiceImplTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllBrands() {
        List<ReadBrandsDTO> expectedBrandsDTO = Arrays.asList(
                new ReadBrandsDTO(1L, "Brand1", "image1"),
                new ReadBrandsDTO(2L, "Brand2", "image2")
        );

        when(brandRepository.findAllBrands()).thenReturn(expectedBrandsDTO);

        List<ReadBrandsDTO> actualBrandsDTO = brandService.getAllBrands();
        assertEquals(expectedBrandsDTO.size(), actualBrandsDTO.size());
    }

    @Test
    public void testGetBrandById() {
        Long brandId = 1L;
        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setName("Brand1");
        brand.setImageUrl("image1");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

        ReadBrandsDTO readBrandsDTO = brandService.getBrandById(brandId);

        assertNotNull(readBrandsDTO);
        assertEquals(brand.getId(), readBrandsDTO.getId());
        assertEquals(brand.getName(), readBrandsDTO.getName());
        assertEquals(brand.getImageUrl(), readBrandsDTO.getImageUrl());
    }

    @Test
    public void testGetBrandByNonExistingId() {
        Long brandId = 1L;
        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setName("Brand1");
        brand.setImageUrl("image1");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

        ReadBrandsDTO readBrandsDTO = brandService.getBrandById(2L);

        assertNull(readBrandsDTO);
    }

    @Test
    public void testEdit() {
        Long brandId = 1L;
        ReadBrandsDTO readBrandsDTO = new ReadBrandsDTO(brandId, "Brand1", "image1");
        Brand existingBrand = new Brand();
        existingBrand.setId(brandId);
        existingBrand.setName("OldBrand");
        existingBrand.setImageUrl("oldImage");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = brandService.edit(readBrandsDTO);
        assertTrue(result);
        assertEquals(brandId, existingBrand.getId());
        assertEquals("Brand1", existingBrand.getName());
        assertEquals("image1", existingBrand.getImageUrl());
    }

    @Test
    public void testEditWithNonExistingBrand() {
        Long brandId = 1L;
        ReadBrandsDTO readBrandsDTO = new ReadBrandsDTO(brandId, "Brand1", "image1");
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        boolean result = brandService.edit(readBrandsDTO);
        assertFalse(result);
    }

    @Test
    public void testGetImageUrlById() {
        Long brandId = 1L;
        Brand brand = new Brand();
        brand.setId(brandId);
        brand.setImageUrl("image1");

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));

        String imageUrl = brandService.getImageUrlById(brandId);
        assertEquals("image1", imageUrl);
    }

    @Test
    public void testGetImageUrlByIdWithNonExistingBrand() {
        Long brandId = 1L;
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        String imageUrl = brandService.getImageUrlById(brandId);
        assertNull(imageUrl);
    }

    @Test
    public void testRemove() {
        Long brandId = 1L;

        brandService.remove(brandId);
        verify(brandRepository, times(1)).deleteById(brandId);
    }

    @Test
    public void testAdd() {
        ReadBrandsDTO readBrandsDTO = new ReadBrandsDTO(null, "Brand1", "image1");
        when(brandRepository.findByName("Brand1")).thenReturn(Optional.empty());
        when(brandRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        boolean result = brandService.add(readBrandsDTO);
        assertTrue(result);
    }

    @Test
    public void testAddWithExistingBrand() {
        ReadBrandsDTO readBrandsDTO = new ReadBrandsDTO(null, "Brand1", "image1");
        when(brandRepository.findByName("Brand1")).thenReturn(Optional.of(new Brand()));

        boolean result = brandService.add(readBrandsDTO);
        assertFalse(result);
    }

    @Test
    public void testGetAll() {
        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setName("Brand1");
        brand1.setImageUrl("image1");

        Brand brand2 = new Brand();
        brand2.setId(2L);
        brand2.setName("Brand2");
        brand2.setImageUrl("image2");

        List<Brand> allBrands = Arrays.asList(
                brand1, brand2
        );

        List<ReadBrandsDTO> expectedBrandsDTO = Arrays.asList(
                new ReadBrandsDTO(1L, "Brand1", "image1"),
                new ReadBrandsDTO(2L, "Brand2", "image2")
        );

        when(brandRepository.findAll()).thenReturn(allBrands);
        when(modelMapper.map(allBrands, ReadBrandsDTO[].class)).thenReturn(expectedBrandsDTO.toArray(new ReadBrandsDTO[0]));
        List<ReadBrandsDTO> actualBrandsDTO = brandService.getAll();

        assertEquals(expectedBrandsDTO.size(), actualBrandsDTO.size());
        assertEquals(expectedBrandsDTO, actualBrandsDTO);
    }
}
