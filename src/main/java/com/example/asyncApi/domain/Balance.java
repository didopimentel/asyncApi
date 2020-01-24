package com.example.asyncApi.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "balance")
public class Balance {

  @Id
  @GeneratedValue
  private long Id;
  
  private float total;
  
  @OneToOne(mappedBy = "balance", cascade = CascadeType.ALL)
  private User user;

  public Balance(float total) {
    this.total = total;
  }

}
