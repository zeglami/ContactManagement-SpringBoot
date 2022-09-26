package me.zegit.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import me.zegit.entities.AddressEntity;
import me.zegit.entities.UserEntity;




@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    
	List<AddressEntity> findByUser(UserEntity currentUser);
	
	AddressEntity findByAddressId(String addressId);
}
