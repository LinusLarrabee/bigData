from superset.utils.core import DB_CONNECTION_MUTATOR

SQLALCHEMY_DATABASE_URI = 'postgresql+psycopg2://superset:superset@superset_postgres:5432/superset'
SECRET_KEY = 'supersecretkey'
SQLALCHEMY_TRACK_MODIFICATIONS = True

class MutableConnectionParams(DB_CONNECTION_MUTATOR):
    def __call__(self, conn_params):
        conn_params['query']['options'] = '-c search_path=superset'
        return conn_params

SQLALCHEMY_CUSTOM_MUTATORS = (MutableConnectionParams(),)
SUPERSET_WEBSERVER_PORT = 8088
