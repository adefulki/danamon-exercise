package ade.test.danamon.base

interface BaseUseCase<In, Out> {
    suspend fun execute(input: In): Out
}