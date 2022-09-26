package me.zegit.services.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.zegit.entities.AddressEntity;
import me.zegit.entities.UserEntity;
import me.zegit.repositories.AddressRepository;
import me.zegit.repositories.UserRepository;
import me.zegit.services.AddressService;
import me.zegit.shared.Utils;
import me.zegit.shared.dto.AddressDto;
import me.zegit.shared.dto.UserDto;



@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils util;
	
	@Override
	public List<AddressDto> getAllAddresses(String email) {
		
		UserEntity currentUser = userRepository.findByEmail(email);
		
		List<AddressEntity> addresses = currentUser.getAdmin() == true ? (List<AddressEntity>) addressRepository.findAll() : addressRepository.findByUser(currentUser);
		
		Type listType = new TypeToken<List<AddressDto>>() {}.getType();
		List<AddressDto> addressesDto = new ModelMapper().map(addresses, listType);
		
		return addressesDto;
	}
	
	
	@Override
	public AddressDto createAddress(AddressDto address, String email) {
		
		UserEntity currentUser = userRepository.findByEmail(email);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(currentUser, UserDto.class);
		
		address.setAddressId(util.generateStringId(30));
		address.setUser(userDto);
		
		AddressEntity addressEntity = modelMapper.map(address, AddressEntity.class); 
		
		AddressEntity newAddress = addressRepository.save(addressEntity);
		
		AddressDto addressDto = modelMapper.map(newAddress, AddressDto.class);
		
		return addressDto;
	}
	
	
	@Override
	public AddressDto getAddress(String addressId) {
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		ModelMapper modelMapper = new ModelMapper();
		
		AddressDto addressDto = modelMapper.map(addressEntity, AddressDto.class);
		
		return addressDto;
	}

	@Override
	public void deleteAddress(String addressId) {
		
		AddressEntity address = addressRepository.findByAddressId(addressId);
		
		if(address == null) throw new RuntimeException("Address not found");
		
		addressRepository.delete(address);
	
	}


}
