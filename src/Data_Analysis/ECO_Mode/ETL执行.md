spark-submit --deploy-mode cluster s3://aps1-tauc-data-analysis/script/source-to-ods/extract-s3.py aps1-tauc-data-analysis source/qoe-raw qoe-raw/ods 2024-07-26 2024-08-01

spark-submit --deploy-mode cluster s3://aps1-tauc-data-analysis/script/ods-to-dwd/cal.py aps1-tauc-data-analysis qoe-raw/ods qoe-raw/dwd 2024-07-26 2024-08-01

spark-submit --deploy-mode cluster s3://aps1-tauc-data-analysis/script/cal-dwd-to-dws.py aps1-tauc-data-analysis dwd/qoe-raw dws/qoe 2024-07-26 2024-08-01

spark-submit --deploy-mode cluster s3://aps1-tauc-data-analysis/script/cal-dwd-to-dws.py aps1-tauc-data-analysis dws/qoe-raw ads/qoe 2024-07-26 2024-08-01





ODS-DWD

spark-submit --deploy-mode cluster s3://aps1-tauc-data-analysis/script/source-to-ods/extract-s3.py aps1-tauc-data-analysis source/qoe-raw qoe-raw/dwd 2024-07-26 2024-08-01



DWD-DWS

需要t+2执行：

spark-submit cal-qoe-dwm.py aps1-tauc-data-analysis qoe-raw/dwd/ap_data qoe-raw/dwm 2024-09-09 2024-09-09

spark-submit cal-client-dwm.py aps1-tauc-data-analysis qoe-raw/dwd qoe-raw/dwm 2024-09-09 2024-09-09

spark-submit cal-ap-avg-s3.py aps1-tauc-data-analysis qoe-raw/dwm qoe-raw/dws non_controller,controller,wireless_data,wire_data 2024-09-09 2024-09-09



spark-submit cal-ap-avg-s3.py aps1-tauc-data-analysis qoe-raw/dwm qoe-raw/dws wireless_data 2024-09-09 2024-09-09



DWS-ADS

spark-submit cal-ap-ads.py aps1-tauc-data-analysis qoe-raw/dws qoe-raw/ads hourly,daily 2024-09-09 2024-09-09 





