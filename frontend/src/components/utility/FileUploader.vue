<template>
    <div class="max-w-7xl mx-auto p-12 text-gray-600">
        <div>
            <label for="bankAccount">Select Bank Account:</label>
            <select v-model="selectedAccountId">
                <option v-for="account in accounts" :key="account.id" :value="account.id">
                    {{ account.name }}
                </option>
            </select>
        </div>
        <input type="file" @change="onFileChange" />
    </div>
</template>

<script>
import axios from 'axios';
import authService from '@/services/authService';

export default {
    data() {
        return {
            accounts: [],
            selectedAccountId: null,
        };
    },
    created() {
        this.fetchBankAccounts();
    },
    methods: {
        fetchBankAccounts() {
            axios
                .get('http://localhost:8080/api/bank-accounts', {
                    headers: { Authorization: 'Bearer ' + authService.getCurrentUser().jwt },
                })
                .then((response) => {
                    this.accounts = response.data;
                })
                .catch((error) => {
                    console.error('Error fetching bank accounts', error);
                });
        },
        onFileChange(event) {
            const file = event.target.files[0];
            this.uploadFile(file);
        },
        async uploadFile(file) {
            if (!this.selectedAccountId) {
                alert('Please select a bank account.');
                return;
            }

            const formData = new FormData();
            formData.append('file', file);
            formData.append('accountId', this.selectedAccountId);

            try {
                const response = await axios.post('http://localhost:8080/api/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data',
                        Authorization: 'Bearer ' + authService.getCurrentUser().jwt,
                    },
                });
                console.log('File uploaded successfully', response.data);
            } catch (error) {
                console.error('Error uploading file', error);
            }
        },
    },
};
</script>
