/**
 * 
 * Copyright 2018 paolo mococci
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package local.example.draft.data.repo;

import java.util.List;
import local.example.draft.data.entity.EndUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author paolo mococci
 */

public interface EndUserRepo 
        extends JpaRepository<EndUserEntity, Long> {
    
    List<EndUserEntity> findById(long id);
    
    @Query(nativeQuery = true, value = "SELECT * FROM ENDUSERENTITY WHERE NAME LIKE ?1%")
    List<EndUserEntity> likeByName(String name);
    
    @Query(nativeQuery = true, value = "SELECT * FROM ENDUSERENTITY WHERE SURNAME LIKE ?1%")
    List<EndUserEntity> likeBySurname(String surname);
    
    @Query(nativeQuery = true, value = "SELECT * FROM ENDUSERENTITY WHERE USERNAME LIKE ?1%")
    List<EndUserEntity> likeByUsername(String username);
}
