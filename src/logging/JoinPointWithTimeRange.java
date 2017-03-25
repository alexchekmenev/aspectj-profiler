package logging;

import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Arrays;

public class JoinPointWithTimeRange {
    private ProceedingJoinPoint joinPoint;
    private long start;
    private long end;

    public JoinPointWithTimeRange(ProceedingJoinPoint joinPoint) {
        this.joinPoint = joinPoint;
    }

    public ProceedingJoinPoint getJoinPoint() {
        return joinPoint;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return joinPoint.getSignature().getName() +
                Arrays.toString(joinPoint.getArgs()) +
                " - " + (getEnd() - getStart()) + " ms.";
    }
}
