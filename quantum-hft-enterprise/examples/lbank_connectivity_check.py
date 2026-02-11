#!/usr/bin/env python3
"""
Lbank API Connectivity Check Script

This script validates the LBANK_API_KEY environment variable and checks
connectivity to the Lbank exchange API.

Usage:
    export LBANK_API_KEY=your-api-key-here
    python lbank_connectivity_check.py

Environment Variables:
    LBANK_API_KEY: Your Lbank API key (required)
"""

import os
import sys
import requests
import json
from datetime import datetime


def check_environment_variable():
    """Check if LBANK_API_KEY environment variable is set."""
    api_key = os.environ.get('LBANK_API_KEY')
    
    if not api_key:
        print("❌ ERROR: LBANK_API_KEY environment variable is not set!")
        print("Please set it using: export LBANK_API_KEY=your-api-key-here")
        return None
    
    print(f"✅ LBANK_API_KEY is set: {api_key[:8]}...{api_key[-8:]}")
    return api_key


def check_lbank_api_connectivity():
    """Check connectivity to Lbank API."""
    print("\n🔍 Checking Lbank API connectivity...")
    
    # Lbank API base URL
    base_url = "https://api.lbkex.com"
    
    try:
        # Test public endpoint - get server time
        print(f"   Testing public endpoint: {base_url}/v2/timestamp.do")
        response = requests.get(f"{base_url}/v2/timestamp.do", timeout=10)
        
        if response.status_code == 200:
            data = response.json()
            print(f"✅ Successfully connected to Lbank API")
            print(f"   Server timestamp: {data}")
            return True
        else:
            print(f"❌ API request failed with status code: {response.status_code}")
            print(f"   Response: {response.text}")
            return False
            
    except requests.exceptions.ConnectionError as e:
        print(f"❌ Connection error: Unable to reach Lbank API")
        print(f"   Error: {str(e)}")
        return False
    except requests.exceptions.Timeout:
        print(f"❌ Timeout: Lbank API did not respond within 10 seconds")
        return False
    except Exception as e:
        print(f"❌ Unexpected error: {str(e)}")
        return False


def check_currency_pairs():
    """Get available currency pairs from Lbank."""
    print("\n📊 Fetching available currency pairs...")
    
    base_url = "https://api.lbkex.com"
    
    try:
        response = requests.get(f"{base_url}/v2/currencyPairs.do", timeout=10)
        
        if response.status_code == 200:
            data = response.json()
            if data and isinstance(data, list):
                print(f"✅ Found {len(data)} available trading pairs")
                print(f"   Sample pairs: {', '.join(data[:5])}")
                return True
            else:
                print(f"⚠️  Unexpected response format: {data}")
                return False
        else:
            print(f"❌ Failed to fetch currency pairs: {response.status_code}")
            return False
            
    except Exception as e:
        print(f"❌ Error fetching currency pairs: {str(e)}")
        return False


def check_ticker_info():
    """Get ticker information for a sample trading pair."""
    print("\n📈 Checking ticker information (eth_usdt)...")
    
    base_url = "https://api.lbkex.com"
    
    try:
        # Get ticker for ETH/USDT pair
        response = requests.get(
            f"{base_url}/v2/ticker.do",
            params={"symbol": "eth_usdt"},
            timeout=10
        )
        
        if response.status_code == 200:
            data = response.json()
            if data and "data" in data:
                ticker_data = data["data"][0]
                print(f"✅ Successfully retrieved ticker data:")
                print(f"   Symbol: {ticker_data.get('symbol', 'N/A')}")
                print(f"   Latest Price: {ticker_data.get('ticker', {}).get('latest', 'N/A')}")
                print(f"   24h Volume: {ticker_data.get('ticker', {}).get('vol', 'N/A')}")
                return True
            else:
                print(f"⚠️  Unexpected ticker response format")
                return False
        else:
            print(f"❌ Failed to fetch ticker info: {response.status_code}")
            return False
            
    except Exception as e:
        print(f"❌ Error fetching ticker info: {str(e)}")
        return False


def main():
    """Main function to run all connectivity checks."""
    print("=" * 60)
    print("Lbank API Connectivity Check")
    print("=" * 60)
    print(f"Timestamp: {datetime.now().isoformat()}")
    
    # Check environment variable
    api_key = check_environment_variable()
    if not api_key:
        sys.exit(1)
    
    # Run connectivity checks
    checks = [
        check_lbank_api_connectivity(),
        check_currency_pairs(),
        check_ticker_info()
    ]
    
    # Summary
    print("\n" + "=" * 60)
    print("Summary:")
    print("=" * 60)
    
    passed = sum(checks)
    total = len(checks)
    
    print(f"Checks passed: {passed}/{total}")
    
    if passed == total:
        print("✅ All connectivity checks passed!")
        print("\n💡 Note: This script validates public API endpoints.")
        print("   For authenticated endpoints, additional configuration is needed.")
        sys.exit(0)
    else:
        print("❌ Some connectivity checks failed.")
        print("   Please verify your network connection and Lbank API status.")
        sys.exit(1)


if __name__ == "__main__":
    main()
