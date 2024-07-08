[ec2-user@ip-172-31-29-173 test]$ docker logs superset

logging was configured successfully

2024-07-08 02:32:40,645:INFO:superset.utils.logging_configurator:logging was configured successfully

2024-07-08 02:32:40,654:INFO:root:Configured event logger of type <class 'superset.utils.log.DBEventLogger'>

We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

2024-07-08 02:32:40,654:WARNING:superset.initialization:We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:40,657:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:40,665:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

/usr/local/lib/python3.8/site-packages/flask_appbuilder/models/sqla/interface.py:67: SAWarning: relationship 'SqlaTable.slices' will copy column tables.id to column slices.datasource_id, which conflicts with relationship(s): 'Slice.table' (copies tables.id to slices.datasource_id). If this is not the intention, consider if these relationships should be linked with back_populates, or if viewonly=True should be applied to one or more if they are read-only. For the less common case that foreign key constraints are partially overlapping, the orm.foreign() annotation can be used to isolate the columns that should be written towards.  To silence this warning, add the parameter 'overlaps="table"' to the 'SqlaTable.slices' relationship. (Background on this error at: https://sqlalche.me/e/14/qzyx)

 for prop in class_mapper(obj).iterate_properties:

WARNI [alembic.env] SQLite Database support for metadata databases will     be removed in a future version of Superset.

INFO [alembic.runtime.migration] Context impl SQLiteImpl.

INFO [alembic.runtime.migration] Will assume transactional DDL.

logging was configured successfully

2024-07-08 02:32:46,742:INFO:superset.utils.logging_configurator:logging was configured successfully

2024-07-08 02:32:46,750:INFO:root:Configured event logger of type <class 'superset.utils.log.DBEventLogger'>

We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

2024-07-08 02:32:46,751:WARNING:superset.initialization:We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:46,753:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:46,756:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

/usr/local/lib/python3.8/site-packages/flask_appbuilder/models/sqla/interface.py:67: SAWarning: relationship 'SqlaTable.slices' will copy column tables.id to column slices.datasource_id, which conflicts with relationship(s): 'Slice.table' (copies tables.id to slices.datasource_id). If this is not the intention, consider if these relationships should be linked with back_populates, or if viewonly=True should be applied to one or more if they are read-only. For the less common case that foreign key constraints are partially overlapping, the orm.foreign() annotation can be used to isolate the columns that should be written towards.  To silence this warning, add the parameter 'overlaps="table"' to the 'SqlaTable.slices' relationship. (Background on this error at: https://sqlalche.me/e/14/qzyx)

 for prop in class_mapper(obj).iterate_properties:

Syncing role definition

2024-07-08 02:32:49,215:INFO:superset.security.manager:Syncing role definition

Syncing Admin perms

2024-07-08 02:32:49,226:INFO:superset.security.manager:Syncing Admin perms

Syncing Alpha perms

2024-07-08 02:32:49,332:INFO:superset.security.manager:Syncing Alpha perms

Syncing Gamma perms

2024-07-08 02:32:49,440:INFO:superset.security.manager:Syncing Gamma perms

Syncing granter perms

2024-07-08 02:32:49,550:INFO:superset.security.manager:Syncing granter perms

Syncing sql_lab perms

2024-07-08 02:32:49,654:INFO:superset.security.manager:Syncing sql_lab perms

Fetching a set of all perms to lookup which ones are missing

2024-07-08 02:32:49,759:INFO:superset.security.manager:Fetching a set of all perms to lookup which ones are missing

Creating missing datasource permissions.

2024-07-08 02:32:49,859:INFO:superset.security.manager:Creating missing datasource permissions.

Creating missing database permissions.

2024-07-08 02:32:49,864:INFO:superset.security.manager:Creating missing database permissions.

Cleaning faulty perms

2024-07-08 02:32:49,868:INFO:superset.security.manager:Cleaning faulty perms

logging was configured successfully

2024-07-08 02:32:53,346:INFO:superset.utils.logging_configurator:logging was configured successfully

2024-07-08 02:32:53,354:INFO:root:Configured event logger of type <class 'superset.utils.log.DBEventLogger'>

We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

2024-07-08 02:32:53,355:WARNING:superset.initialization:We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:53,358:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:53,361:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

Recognized Database Authentications.

Error! User already exists admin

/usr/local/lib/python3.8/site-packages/flask_appbuilder/models/sqla/interface.py:67: SAWarning: relationship 'SqlaTable.slices' will copy column tables.id to column slices.datasource_id, which conflicts with relationship(s): 'Slice.table' (copies tables.id to slices.datasource_id). If this is not the intention, consider if these relationships should be linked with back_populates, or if viewonly=True should be applied to one or more if they are read-only. For the less common case that foreign key constraints are partially overlapping, the orm.foreign() annotation can be used to isolate the columns that should be written towards.  To silence this warning, add the parameter 'overlaps="table"' to the 'SqlaTable.slices' relationship. (Background on this error at: https://sqlalche.me/e/14/qzyx)

 for prop in class_mapper(obj).iterate_properties:

logging was configured successfully

2024-07-08 02:32:58,330:INFO:superset.utils.logging_configurator:logging was configured successfully

2024-07-08 02:32:58,337:INFO:root:Configured event logger of type <class 'superset.utils.log.DBEventLogger'>

We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

2024-07-08 02:32:58,338:WARNING:superset.initialization:We haven't found any Content Security Policy (CSP) defined in the configurations. Please make sure to configure CSP using the TALISMAN_ENABLED and TALISMAN_CONFIG keys or any other external software. Failing to configure CSP have serious security implications. Check https://developer.mozilla.org/en-US/docs/Web/HTTP/CSP for more information. You can disable this warning using the CONTENT_SECURITY_POLICY_WARNING key.

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:58,341:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `FILTER_STATE_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

2024-07-08 02:32:58,344:WARNING:superset.utils.cache_manager:Falling back to the built-in cache, that stores data in the metadata database, for the following cache: `EXPLORE_FORM_DATA_CACHE_CONFIG`. It is recommended to use `RedisCache`, `MemcachedCache` or another dedicated caching backend for production deployments

 \* Serving Flask app 'superset.app:create_app()' (lazy loading)

 \* Environment: production

  WARNING: This is a development server. Do not use it in a production deployment.

  Use a production WSGI server instead.

 \* Debug mode: off

/usr/local/lib/python3.8/site-packages/flask_appbuilder/models/sqla/interface.py:67: SAWarning: relationship 'SqlaTable.slices' will copy column tables.id to column slices.datasource_id, which conflicts with relationship(s): 'Slice.table' (copies tables.id to slices.datasource_id). If this is not the intention, consider if these relationships should be linked with back_populates, or if viewonly=True should be applied to one or more if they are read-only. For the less common case that foreign key constraints are partially overlapping, the orm.foreign() annotation can be used to isolate the columns that should be written towards.  To silence this warning, add the parameter 'overlaps="table"' to the 'SqlaTable.slices' relationship. (Background on this error at: https://sqlalche.me/e/14/qzyx)

 for prop in class_mapper(obj).iterate_properties:

2024-07-08 02:32:59,849:INFO:werkzeug: * Running on all addresses (0.0.0.0)

  WARNING: This is a development server. Do not use it in a production deployment.

 \* Running on http://127.0.0.1:8088

 \* Running on http://192.168.224.3:8088 (Press CTRL+C to quit)