package com.mercadolibre.frescos_api_grupo_2_w2.config;

import com.fasterxml.jackson.core.JsonParseException;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.ExceptionDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiError;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.ApiException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.InternalServerErrorException;
import com.mercadolibre.frescos_api_grupo_2_w2.exceptions.UserAlreadyExists;
import com.newrelic.api.agent.NewRelic;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);
	private final MessageSource messageSource;


	public ControllerExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}


	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ApiError> noHandlerFoundException(HttpServletRequest req, NoHandlerFoundException ex) {
		ApiError apiError = new ApiError("route_not_found", String.format("Route %s not found", req.getRequestURI()), HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { ApiException.class })
	protected ResponseEntity<ApiError> handleApiException(ApiException e) {
		Integer statusCode = e.getStatusCode();
		boolean expected = HttpStatus.INTERNAL_SERVER_ERROR.value() > statusCode;
		NewRelic.noticeError(e, expected);
		if (expected) {
			LOGGER.warn("Internal Api warn. Status Code: " + statusCode, e);
		} else {
			LOGGER.error("Internal Api error. Status Code: " + statusCode, e);
		}

		ApiError apiError = new ApiError(e.getCode(), e.getDescription(), statusCode);
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(value = { Exception.class })
	protected ResponseEntity<ApiError> handleUnknownException(Exception e) {
		LOGGER.error("Internal error", e);
		NewRelic.noticeError(e);

		ApiError apiError = new ApiError("internal_error", "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
		return ResponseEntity.status(apiError.getStatus())
				.body(apiError);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ExceptionDTO>> defaultHandler(MethodArgumentNotValidException e){
		BindingResult result = e.getBindingResult();

		List<FieldError> fieldErrors = result.getFieldErrors();
		List<ExceptionDTO> processedFieldErrors = processFieldErrors(fieldErrors);

		return ResponseEntity.badRequest().body(processedFieldErrors);
	}

	@ExceptionHandler(JsonParseException.class)
	public ResponseEntity<ExceptionDTO> defaultHandler(JsonParseException e){
		return ResponseEntity.badRequest().body(new ExceptionDTO("Verifique a formatação do JSON"));
	}

	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<ExceptionDTO> defaultHandler(InternalServerErrorException e){
		return ResponseEntity.badRequest().body(new ExceptionDTO(e.getMessage()));
	}

	@ExceptionHandler(UserAlreadyExists.class)
	public ResponseEntity<ExceptionDTO> defaultHandler(UserAlreadyExists e){
		return ResponseEntity.badRequest().body(new ExceptionDTO(e.getMessage()));
	}

	private List<ExceptionDTO> processFieldErrors(List<FieldError> fieldErrors) {
		List<ExceptionDTO> dtosList = new ArrayList<>();

		for (FieldError fieldError: fieldErrors) {
			String errorMessage = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			dtosList.add(new ExceptionDTO(fieldError.getField(), errorMessage));
		}
		return dtosList;
	}
}