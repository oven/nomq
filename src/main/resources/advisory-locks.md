## Table 9.96. Advisory Lock Functions

```
pg_advisory_lock ( key bigint ) → void
pg_advisory_lock ( key1 integer, key2 integer ) → void
```
Obtains an exclusive session-level advisory lock, waiting if necessary.

```
pg_advisory_lock_shared ( key bigint ) → void
pg_advisory_lock_shared ( key1 integer, key2 integer ) → void
```
Obtains a shared session-level advisory lock, waiting if necessary.

```
pg_advisory_unlock ( key bigint ) → boolean
pg_advisory_unlock ( key1 integer, key2 integer ) → boolean
```
Releases a previously-acquired exclusive session-level advisory lock. Returns true if the lock is successfully released. If the lock was not held, false is returned, and in addition, an SQL warning will be reported by the server.

```
pg_advisory_unlock_all () → void
```
Releases all session-level advisory locks held by the current session. (This function is implicitly invoked at session end, even if the client disconnects ungracefully.)

```
pg_advisory_unlock_shared ( key bigint ) → boolean
pg_advisory_unlock_shared ( key1 integer, key2 integer ) → boolean
```
Releases a previously-acquired shared session-level advisory lock. Returns true if the lock is successfully released. If the lock was not held, false is returned, and in addition, an SQL warning will be reported by the server.

```
pg_advisory_xact_lock ( key bigint ) → void
pg_advisory_xact_lock ( key1 integer, key2 integer ) → void
```
Obtains an exclusive transaction-level advisory lock, waiting if necessary.

```
pg_advisory_xact_lock_shared ( key bigint ) → void
pg_advisory_xact_lock_shared ( key1 integer, key2 integer ) → void
```
Obtains a shared transaction-level advisory lock, waiting if necessary.

```
pg_try_advisory_lock ( key bigint ) → boolean
pg_try_advisory_lock ( key1 integer, key2 integer ) → boolean
```
Obtains an exclusive session-level advisory lock if available. This will either obtain the lock immediately and return true, or return false without waiting if the lock cannot be acquired immediately.

```
pg_try_advisory_lock_shared ( key bigint ) → boolean
pg_try_advisory_lock_shared ( key1 integer, key2 integer ) → boolean
```
Obtains a shared session-level advisory lock if available. This will either obtain the lock immediately and return true, or return false without waiting if the lock cannot be acquired immediately.

```
pg_try_advisory_xact_lock ( key bigint ) → boolean
pg_try_advisory_xact_lock ( key1 integer, key2 integer ) → boolean
```
Obtains an exclusive transaction-level advisory lock if available. This will either obtain the lock immediately and return true, or return false without waiting if the lock cannot be acquired immediately.

```
pg_try_advisory_xact_lock_shared ( key bigint ) → boolean
pg_try_advisory_xact_lock_shared ( key1 integer, key2 integer ) → boolean
```
Obtains a shared transaction-level advisory lock if available. This will either obtain the lock immediately and return true, or return false without waiting if the lock cannot be acquired immediately.
