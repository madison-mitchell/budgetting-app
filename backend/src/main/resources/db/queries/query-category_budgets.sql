-- Budgets
SELECT 
	b.id, r.full_name, b.year, b.month, b.budget_amount, b.amount_spent, b.user_id
FROM 
	category_budget b
JOIN category_parent_child_relation r ON r.id = b.category_id
ORDER BY 
	month DESC;