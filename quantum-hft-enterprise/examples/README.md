# Lbank Exchange Connectivity Examples

This directory contains scripts and examples for connecting to the Lbank exchange API.

## Prerequisites

Install the required Python dependencies:

```bash
pip install -r requirements.txt
```

## Scripts

### lbank_connectivity_check.py

A connectivity validation script that checks:
- Environment variable configuration (LBANK_API_KEY)
- Lbank API endpoint connectivity
- Available trading pairs
- Sample ticker data retrieval

#### Usage

```bash
export LBANK_API_KEY=your-api-key-here
python lbank_connectivity_check.py
```

#### Example Output

```
============================================================
Lbank API Connectivity Check
============================================================
Timestamp: 2026-02-11T05:48:32.355814
✅ LBANK_API_KEY is set: 59791b0d...1baa2358

🔍 Checking Lbank API connectivity...
   Testing public endpoint: https://api.lbkex.com/v2/timestamp.do
✅ Successfully connected to Lbank API
   Server timestamp: {'timestamp': '1707629312355', 'timezone': 'GMT+08:00'}

📊 Fetching available currency pairs...
✅ Found 200+ available trading pairs
   Sample pairs: eth_usdt, btc_usdt, ada_usdt, xrp_usdt, doge_usdt

📈 Checking ticker information (eth_usdt)...
✅ Successfully retrieved ticker data:
   Symbol: eth_usdt
   Latest Price: 2850.45
   24h Volume: 123456.78

============================================================
Summary:
============================================================
Checks passed: 3/3
✅ All connectivity checks passed!

💡 Note: This script validates public API endpoints.
   For authenticated endpoints, additional configuration is needed.
```

## Lbank API Documentation

For more information about the Lbank API, visit:
- API Documentation: https://www.lbank.com/en-US/docs/index.html
- Developer Support: https://www.lbank.com

## Integration with jLOB

The Lbank API key is configured in the main jLOB application through:
1. The `config.yaml` or `config.local.yaml` configuration files
2. The `LBANK_API_KEY` environment variable (takes precedence)

The configuration is loaded by the `Config` class in `src/main/java/config/Config.java`.
