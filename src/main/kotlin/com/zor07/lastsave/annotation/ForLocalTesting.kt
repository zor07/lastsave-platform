package com.zor07.lastsave.annotation

/**
 * Marks methods added solely for local testing convenience.
 * Not intended for production use.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class ForLocalTesting
