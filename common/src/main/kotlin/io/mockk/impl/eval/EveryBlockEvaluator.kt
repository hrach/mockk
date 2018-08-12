package io.mockk.impl.eval

import io.mockk.CapturingSlot
import io.mockk.MockKGateway.CallRecorder
import io.mockk.MockKGateway.Stubber
import io.mockk.MockKMatcherScope
import io.mockk.MockKStubScope
import io.mockk.impl.recording.AutoHinter
import io.mockk.proxy.MockKInterceptionScope

class EveryBlockEvaluator(
    callRecorder: () -> CallRecorder,
    autoHinterFactory: () -> AutoHinter,
    intereceptionScope: MockKInterceptionScope
) : RecordedBlockEvaluator(
    callRecorder,
    autoHinterFactory,
    intereceptionScope
), Stubber {

    override fun <T> every(
        mockBlock: (MockKMatcherScope.() -> T)?,
        coMockBlock: (suspend MockKMatcherScope.() -> T)?
    ): MockKStubScope<T, T> {

        callRecorder().startStubbing()

        val lambda = CapturingSlot<Function<*>>()
        val scope = MockKMatcherScope(callRecorder(), lambda)

        record(scope, mockBlock, coMockBlock)

        return MockKStubScope(callRecorder(), lambda)
    }
}
