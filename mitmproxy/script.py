#script.py
from mitmproxy import http

def request(flow:http.HTTPFlow)->None:
    # qing
    flow.request.query["mitmproxy"]="rocks"
    flow.request.headers[""]=""
    flow.request.

def response(flow:http.HTTPFlow)->None:
    flow.response.headers['newheader']="foo"
    print(flow.response.text)
