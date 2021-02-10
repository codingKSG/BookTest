package com.cos.book.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity // 서버 실행시에 테이블이 MySQL에 생성됨
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 데이터 베이스 번호증가 전략을 선택
	private Long id;
	
	private String title;
	private Double rating;
	private Double price;
}
