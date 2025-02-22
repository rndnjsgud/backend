package com.arom.yeojung.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  // Global

  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다."),

  INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

  //DailyPlan

  TOTAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 TotalPlan을 찾을 수 없습니다."),

  DAILY_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 DailyPlan을 찾을 수 없습니다."),

  DAILY_PLAN_NOT_IN_TOTAL_PLAN(HttpStatus.BAD_REQUEST, "DailyPlan이 해당 TotalPlan에 속해 있지 않습니다.");


  private final HttpStatus status;
  private final String message;

}