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

  //diary
  DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Diary를 찾을 수 없습니다."),
  DIARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 컨텐츠를 찾을 수 없습니다."),
  DIARY_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 Diary입니다."),

  //UserDiary
  USERDIAYR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저 다이어리 관계를 찾을 수 없습니다."),

  //file
  FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 File을 찾을 수 없습니다."),
  FILE_SIZE_EXCEED(HttpStatus.PAYLOAD_TOO_LARGE, "업로드 가능한 파일 크기를 초과했습니다."),
  FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

  //DailyPlan
  TOTAL_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 TotalPlan을 찾을 수 없습니다."),

  DAILY_PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 DailyPlan을 찾을 수 없습니다."),

  DAILY_PLAN_NOT_IN_TOTAL_PLAN(HttpStatus.BAD_REQUEST, "DailyPlan이 해당 TotalPlan에 속해 있지 않습니다."),

  //CheckList
  CHECKLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 CheckList를 찾을 수 없습니다."),

  //Location
  LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Location을 찾을 수 없습니다");
  //User
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

  //comment
  COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Comment를 찾을 수 없습니다."),

  //like
  LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Like를 찾을 수 없습니다."),

  SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "저장에 실패했습니다.");

  private final HttpStatus status;
  private final String message;
}