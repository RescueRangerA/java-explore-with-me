package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.log.LogFormatUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.practicum.ewm.CustomDateTimeFormatter;
import ru.practicum.ewm.exception.event.EventMutationViolationException;
import ru.practicum.ewm.exception.generic.ExtendedEntityNotFoundException;
import ru.practicum.ewm.exception.request.EventRequestMutationViolationException;
import ru.practicum.ewm.controller.dto.ApiError;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    private final CustomDateTimeFormatter dateTimeFormatter;

    public ExceptionControllerAdvice(CustomDateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @ExceptionHandler({EventMutationViolationException.class, EventRequestMutationViolationException.class})
    public ResponseEntity<ApiError> handleEventUpdateViolation(final Exception e) {
        logIfNeeded(e);

        return new ResponseEntity<>(
                new ApiError(
                        exceptionToListOfErrors(e),
                        e.getMessage(),
                        "For the requested operation the conditions are not met.",
                        ApiError.StatusEnum._403_FORBIDDEN,
                        dateTimeFormatter.format(LocalDateTime.now())
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException e) {
        logIfNeeded(e);

        return new ResponseEntity<>(
                new ApiError(
                        exceptionToListOfErrors(e),
                        e.getMessage(),
                        "Incorrectly made request.",
                        ApiError.StatusEnum._400_BAD_REQUEST,
                        dateTimeFormatter.format(LocalDateTime.now())
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleDateIntegrityViolation(final DataIntegrityViolationException e) {
        logIfNeeded(e);

        return new ResponseEntity<>(
                new ApiError(
                        exceptionToListOfErrors(e),
                        e.getMessage(),
                        "Integrity constraint has been violated.",
                        ApiError.StatusEnum._409_CONFLICT,
                        dateTimeFormatter.format(LocalDateTime.now())
                ),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFound(final ExtendedEntityNotFoundException e) {
        logIfNeeded(e);

        return new ResponseEntity<>(
                new ApiError(
                        exceptionToListOfErrors(e),
                        e.getMessage(),
                        "The required object was not found.",
                        ApiError.StatusEnum._404_NOT_FOUND,
                        dateTimeFormatter.format(LocalDateTime.now())
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public void handleValidationException(final AccessDeniedException e, HttpServletResponse response) throws IOException {
        logIfNeeded(e);
        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler
    public ModelAndView handleException(
            HttpServletRequest request,
            HttpServletResponse response,
            @Nullable Object handler,
            Exception e
    ) throws IOException {
        ModelAndView mav = (new DefaultHandlerExceptionResolver()).resolveException(request, response, handler, e);

        if (mav != null) {
            return mav;
        }

        logIfNeeded(e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        return new ModelAndView();
    }

    protected void logIfNeeded(Exception e) {
        if (log != null && log.isWarnEnabled()) {
            log.warn(buildLogMessage(e));
        }
    }

    protected String buildLogMessage(Exception e) {
        return "Resolved [" + LogFormatUtils.formatValue(e, -1, true) + "]";
    }

    protected List<String> exceptionToListOfErrors(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return List.of(sw.toString());
    }
}
