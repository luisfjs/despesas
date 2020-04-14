package br.com.lsena.despesas.config;

import br.com.lsena.despesas.LogarExecucao;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class AspectConfig {
    @Around("@annotation(logarExecucao)")
    public Object logarExecucao(final ProceedingJoinPoint pjp, final LogarExecucao logarExecucao) throws Throwable {

        final String classe = pjp.getSignature().getDeclaringTypeName();
        final String metodo = pjp.getSignature().getName();

        log.debug("INICIO :: CLASSE {} :: METODO {} :: PARAMETROS {}", classe, metodo, pjp.getArgs());
        final StopWatch monitor = new StopWatch(classe);
        monitor.start(metodo);

        final Object saida = pjp.proceed();

        monitor.stop();
        log.info("FIM :: CLASSE {} :: METODO {} :: EXECUCAO {} ms", classe, metodo, monitor.getTotalTimeMillis());

        return saida;
    }
}
