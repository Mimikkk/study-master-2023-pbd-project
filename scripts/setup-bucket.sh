curl http://www.cs.put.poznan.pl/kjankiewicz/bigdata/stream_project/stocks_result.zip | gsutil cp - gs://"${BUCKET_NAME}"/resources/datasets/stock-prices/parts.zip
curl http://www.cs.put.poznan.pl/kjankiewicz/bigdata/stream_project/symbols_valid_meta.csv | gsutil cp - gs://"${BUCKET_NAME}"/resources/datasets/stock-prices/meta.csv
