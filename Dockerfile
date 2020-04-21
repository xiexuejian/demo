FROM python:3.7.1-alpine
RUN mkdir /test
WORKDIR /test
COPY ./ /test
EXPOSE 8000
CMD ["python","-m","http.server"]