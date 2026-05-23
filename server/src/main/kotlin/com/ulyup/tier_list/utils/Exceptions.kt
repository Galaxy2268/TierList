package com.ulyup.tier_list.utils

class BadRequestException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String = "Unauthorized") : RuntimeException(message)
class ForbiddenException(message: String) : RuntimeException(message)
class NotFoundException(message: String) : RuntimeException(message)
class ConflictException(message: String) : RuntimeException(message)
class CapReachedException(message: String) : RuntimeException(message)