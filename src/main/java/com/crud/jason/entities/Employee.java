/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.crud.jason.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author Alisher Urunov
 */
@Getter
@Setter
@Entity
public class Employee extends ResourceSupport {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  public Long employeeId;

  @NotNull
  @NotBlank
  @Length(max = 40)
  public String name;

  @NotNull
  @NotBlank
  @Length(max = 40)
  public String surname;

  @NotNull
  @NotBlank
  @Email
  @Length(max = 40)
  public String email;

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Employee) {
      Employee comparedObject = (Employee) obj;
      return comparedObject.getEmail().equals(this.email)
          && comparedObject.getName().equals(this.name)
          && comparedObject.getSurname().equals(this.surname);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return name.hashCode() + name.hashCode() + email.hashCode();
  }
}
