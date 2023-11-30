package bg.softuni.carsHeaven.service.impl;

import bg.softuni.carsHeaven.model.dtos.cars.ReadBrandsDTO;
import bg.softuni.carsHeaven.model.entity.Brand;
import bg.softuni.carsHeaven.repository.BrandRepository;
import bg.softuni.carsHeaven.service.BrandService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;

    public BrandServiceImpl(BrandRepository brandRepository,
                            ModelMapper modelMapper) {
        this.brandRepository = brandRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReadBrandsDTO> getAllBrands() {
        List<ReadBrandsDTO> allBrandsDTO = this.brandRepository.findAllBrands();
        allBrandsDTO.sort(Comparator.comparing(ReadBrandsDTO::getName));
        return allBrandsDTO;
    }

    @Override
    public ReadBrandsDTO getBrandById(Long brandId) {
        if (this.brandRepository.findById(brandId).isPresent()) {
            Brand brand = this.brandRepository.findById(brandId).get();
            ReadBrandsDTO readBrandsDTO = new ReadBrandsDTO();
            readBrandsDTO.setName(brand.getName());
            readBrandsDTO.setId(brandId);
            readBrandsDTO.setImageUrl(brand.getImageUrl());
            return readBrandsDTO;
        }
        return null;
    }

    @Override
    public boolean edit(ReadBrandsDTO readBrandsDTO) {
        Optional<Brand> byId = this.brandRepository.findById(readBrandsDTO.getId());
        if (byId.isEmpty()) {
            return false;
        }
        Brand brand = byId.get();
        brand.setImageUrl(readBrandsDTO.getImageUrl());
        if (readBrandsDTO.getName() != null) {
            brand.setName(readBrandsDTO.getName());
        }
        this.brandRepository.save(brand);
        return true;
    }

    @Override
    public String getImageUrlById(Long brandId) {
        Optional<Brand> byId = this.brandRepository.findById(brandId);
        if (byId.isEmpty()) {
            return null;
        }
        return byId.get().getImageUrl();
    }

    @Override
    public void remove(Long brandId) {
        this.brandRepository.deleteById(brandId);
    }

    @Override
    public boolean add(ReadBrandsDTO readBrandsDTO) {
        Optional<Brand> byName = this.brandRepository.findByName(readBrandsDTO.getName());
        if (byName.isPresent()) {
            return false;
        }
        Brand brand = new Brand();
        brand.setName(readBrandsDTO.getName());
        brand.setImageUrl(readBrandsDTO.getImageUrl());

        this.brandRepository.save(brand);
        return true;
    }

    @Override
    public List<ReadBrandsDTO> getAll() {
        List<Brand> allBrands = this.brandRepository.findAll();
        return Arrays.stream(this.modelMapper.map(allBrands, ReadBrandsDTO[].class)).toList();
    }
}
